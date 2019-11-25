package com.example.zsl.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.Exception


class BasePresenter<T : IBaseView> : IBasePresenter<T> {

    var mRootView : T ?= null

    private var mComSub = CompositeDisposable()

    override fun attachView(mRootView: T) {
        this.mRootView = mRootView
    }

    override fun detachView() {
        if (mRootView != null) {
            mRootView = null
        }

        if (!mComSub.isDisposed){
            mComSub.dispose()
            mComSub.clear()
        }
    }

    fun isAttachView() :Boolean{
        val attach = this.mRootView != null
        if (!attach){
            throw Exception("View is not attach")
        }
        return attach
    }

    fun addSubscription(disposable: Disposable){
        mComSub.add(disposable)
    }
}