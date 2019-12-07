package com.example.zsl.rx.rxbus

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/4 14:29.
 * Modify by zsl on 2019/12/4 14:29.
 * Version 1.0
 */
interface IEventBus {

    fun post(any: Any) : Any

    fun postSticky(any: Any)

    fun hasObservers() : Boolean
}