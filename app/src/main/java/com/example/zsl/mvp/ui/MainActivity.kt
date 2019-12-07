package com.example.zsl.mvp.ui

import com.example.zsl.R
import com.example.zsl.base.BaseActivity
import com.example.zsl.bean.HomeBean
import com.example.zsl.mvp.contract.MainContract
import com.example.zsl.mvp.presenter.MainPresenter
import com.example.zsl.net.MyObserver
import com.example.zsl.net.RetrofitManager
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils

class MainActivity : BaseActivity<MainContract.View,MainContract.Presenter>(), MainContract.View{


    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter(): MainContract.Presenter{
        return MainPresenter()
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun startRequest() {

    }
}
