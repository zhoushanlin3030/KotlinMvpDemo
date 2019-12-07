package com.hazz.kotlinmvp.rx.scheduler

import com.example.zsl.rx.scheduler.BaseScheduler
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by xuhao on 2017/11/17.
 * desc:
 */

object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

    fun <T> ioToMain(act: RxAppCompatActivity) : BaseScheduler<T> {
        return object : BaseScheduler<T>(Schedulers.io(),AndroidSchedulers.mainThread()){
            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                return upstream.compose(act.bindToLifecycle())
            }
        }
    }
}
