package com.example.zsl.net

import com.example.zsl.MyApplication
import com.example.zsl.net.exception.ErrorStatus
import com.example.zsl.net.exception.ExceptionHandler
import com.example.zsl.utils.NetworkUtil
import io.reactivex.observers.DisposableObserver

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/3 14:44.
 * Modify by zsl on 2019/12/3 14:44.
 * Version 1.0
 */
abstract class NormalObserver<T> : DisposableObserver<BaseResponse<T>>(){

    override fun onStart() {
        super.onStart()
        if (!NetworkUtil.isNetworkAvailable(MyApplication.context)){
            //TODO toast
            return
        }
    }

    override fun onNext(t: BaseResponse<T>) {
        if (t.code == ErrorStatus.SUCCESS){
            onSuccess(t)
        } else {
            onFailure(t.msg)
            onFailure(t.code,t.msg)
        }
    }

    override fun onError(e: Throwable) {
        val handleException = ExceptionHandler.handleException(e)
        handleException.msg?.let { onFailure(it) }
        handleException.msg?.let { handleException.code?.let { it1 -> onFailure(it1, it) } }
    }

    override fun onComplete() {
        onFinish()
    }

    protected abstract fun onSuccess(data : BaseResponse<T>)

    protected open fun onFailure(message: String){}

    protected open fun onFailure(code : Int, message : String){}

    protected open fun onFinish(){}
}