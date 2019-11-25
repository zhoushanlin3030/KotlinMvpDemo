package com.example.zsl.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.classic.common.MultipleStatusView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.StringBuilder

abstract class BaseActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks{

    /**
     * 多状态view切换
     */
    protected var mMultipleStatusView:MultipleStatusView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initData()
        initView()
        startRequest()
        setRetryListener()
    }

    open val mRetryClickListener : View.OnClickListener = View.OnClickListener {
        startRequest()
    }

    private fun setRetryListener(){
        mMultipleStatusView?.setOnClickListener(mRetryClickListener)
    }

    /**
     * 加载布局
     */
    abstract fun layoutId() : Int

    /**
     * 数据初始化
     */
    abstract fun initData()

    /**
     * 界面初始化
     */
    abstract fun initView()

    /**
     * 开始网络请求
     */
    abstract fun startRequest()


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