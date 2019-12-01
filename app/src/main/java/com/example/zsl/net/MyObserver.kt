package com.example.zsl.net

import com.example.zsl.MyApplication
import com.example.zsl.net.exception.ErrorStatus
import com.example.zsl.net.exception.ExceptionHandler
import com.example.zsl.utils.NetworkUtil
import io.reactivex.observers.DisposableObserver

abstract class MyObserver<T> : DisposableObserver<BaseResponse<T>>() {

    override fun onStart() {
        if (!NetworkUtil.isNetworkAvailable(MyApplication.context)){
            //TODO toast 网络异常
            return
        }
    }

    override fun onNext(t: BaseResponse<T>) {
        // TODO token 校验
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
        handleException.code?.let { handleException.message?.let { it1 -> onFailure(it, it1) } }
    }

    override fun onComplete() {
        onFinish()
    }

    protected abstract fun onSuccess(data : BaseResponse<T>)

    protected open fun onFailure(message: String){}

    protected open fun onFailure(code : Int, message : String){}

    protected open fun onFinish(){}
}