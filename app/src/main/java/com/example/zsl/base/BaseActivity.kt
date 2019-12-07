package com.example.zsl.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.classic.common.MultipleStatusView
import com.example.zsl.widget.LoadingDialog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.StringBuilder

abstract class BaseActivity<V:IBaseView,P:IBasePresenter<V>> : RxAppCompatActivity(),EasyPermissions.PermissionCallbacks,IBaseView{

    var mPresenter : P ?= null

    var loadingDialog : Dialog ?= null

    /**
     * 多状态view切换
     */
    protected var mMultipleStatusView:MultipleStatusView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initPresenter()
        mPresenter?.attachView(this as V)
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
     * P层实例初始化
     */
    abstract fun initPresenter() : P

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

    override fun showLoading() {
        loadingDialog = LoadingDialog.show(this,loadingDialog)
    }

    override fun hideLoading() {
        loadingDialog ?.let {
            loadingDialog!!.setOnDismissListener(null)
        }
        LoadingDialog.dismiss(loadingDialog)
    }

    override fun <T> bindToLifecycleDestory(): LifecycleTransformer<T> {
        return bindUntilEvent(ActivityEvent.DESTROY)
    }

    override fun <T> bindToDefaultLifecycle(): LifecycleTransformer<T> {
        return bindToLifecycle()
    }

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

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null){
            mPresenter!!.detachView()
            mPresenter = null
        }
    }

}