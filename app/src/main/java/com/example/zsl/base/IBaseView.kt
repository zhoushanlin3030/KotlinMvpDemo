package com.example.zsl.base

import com.trello.rxlifecycle2.LifecycleTransformer

interface IBaseView {

    fun showLoading()

    fun hideLoading()

    fun <T> bindToDefaultLifecycle() : LifecycleTransformer<T>

    fun <T> bindToLifecycleDestory() : LifecycleTransformer<T>
}