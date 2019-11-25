package com.example.zsl.base

interface IBasePresenter<V:IBaseView> {

    fun attachView(mRootView:V)

    fun detachView()

}