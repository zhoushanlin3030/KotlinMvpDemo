package com.example.zsl.mvp.contract

import com.example.zsl.base.IBasePresenter
import com.example.zsl.base.IBaseView

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/4 11:27.
 * Modify by zsl on 2019/12/4 11:27.
 * Version 1.0
 */
interface MainContract {

    interface View : IBaseView{

    }

    interface Presenter : IBasePresenter<View>{
        fun initData()
    }
}