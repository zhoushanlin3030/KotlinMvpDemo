package com.example.zsl.rx.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/4 14:30.
 * Modify by zsl on 2019/12/4 14:30.
 * Version 1.0
 */
class RxBus private constructor() : IEventBus {

    private val mBus : Subject<Any>  by lazy {
        PublishSubject.create<Any>().toSerialized()
    }

    private val mStickEventMap : ConcurrentHashMap<Class<*>,Any> = ConcurrentHashMap()

    companion object {
        val instance : RxBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            RxBus()
        }
    }

    /**
     * 发送事件
     */
    override fun post(any: Any): Any {
        mBus.onNext(any)
        return any
    }

    /**
     * 发送粘性事件
     */
    override fun postSticky(any: Any){
        synchronized(mStickEventMap){
            mStickEventMap.put(any.javaClass,any)
        }
        post(any)
    }

    fun <T> toObservable(eventType: Class<T>) : Observable<T> {
        return mBus.ofType(eventType)
    }

    fun <T> toObservableSticky(eventType : Class<T>) : Observable<T> {
        synchronized(mStickEventMap) {
            val observable = mBus.ofType(eventType)
            val event = mStickEventMap[eventType]
            return if (event != null) {
                observable.mergeWith(Observable.create{
                    it.onNext(eventType.cast(event))
                })
            } else {
                observable
            }
        }
    }

    override fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    fun hasStickyEvent(clazz: Class<*>) : Boolean{
        return mStickEventMap.contains(clazz)
    }

    fun <T> getStickyEvent(event : Class<T>) : T {
        synchronized(mStickEventMap){
            return event.cast(mStickEventMap[event])
        }
    }

    fun <T> removeStickyEvent(event : Class<T>) {
        synchronized(mStickEventMap){
            event.cast(mStickEventMap.remove(event))
        }
    }

    fun removeAllStickyEvents(){
       synchronized(mStickEventMap){
           mStickEventMap.clear()
       }
    }
}