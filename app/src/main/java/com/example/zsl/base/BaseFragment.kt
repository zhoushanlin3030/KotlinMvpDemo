package com.example.zsl.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.classic.common.MultipleStatusView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.StringBuilder

abstract class BaseFragment : Fragment(),EasyPermissions.PermissionCallbacks{

    //界面是否加载完成
    private var mViewInflateFinish = false

    //是否已经请求过数据
    private var hasLoadData = false

    //多状态切换
    protected var mMultipleStatusView : MultipleStatusView ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(layoutId(),null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewInflateFinish = true
        initView()
        lazyLoadIfPrepared()
        setRetryListener()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            lazyLoadIfPrepared()
        }
    }

    private fun lazyLoadIfPrepared(){
        if (mViewInflateFinish && userVisibleHint && !hasLoadData){
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener : View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }

    private fun setRetryListener(){
        mMultipleStatusView?.setOnClickListener(mRetryClickListener)
    }

    /**
     * 布局
     */
    abstract fun layoutId() : Int

    /**
     * 初始化界面
     */
    abstract fun initView()

    /**
     * 懒加载数据
     */
    abstract fun lazyLoad()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //权限申请成功回调 TODO
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //权限被拒绝
        val sb = StringBuilder()
        for (str in perms){
            sb.append(str).append("\n")
        }
        sb.replace(sb.length-2,sb.length,"")

        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要"+sb+"权限,是否打开设置？")
                .setPositiveButton("去设置")
                .setNegativeButton("拒绝")
                .build()
                .show()
        }
    }
}