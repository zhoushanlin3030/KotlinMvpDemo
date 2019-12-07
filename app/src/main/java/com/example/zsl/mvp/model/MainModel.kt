package com.example.zsl.mvp.model

import com.example.zsl.bean.HomeBean
import com.example.zsl.net.RetrofitManager
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/4 14:14.
 * Modify by zsl on 2019/12/4 14:14.
 * Version 1.0
 */
class MainModel{

    fun requestFirstPage(num:Int) : Observable<HomeBean>{
        return RetrofitManager.service.getFirstHomeData(num)
            .compose(SchedulerUtils.ioToMain())
    }
}