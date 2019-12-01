package com.example.zsl.api

import com.example.zsl.bean.HomeBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/11/30 15:15.
 * Modify by zsl on 2019/11/30 15:15.
 * Version 1.0
 */
interface ApiService {

	/**
	 * 首页精选
	 */
	@GET("v2/feed?")
	fun getFirstHomeData(@Query("num") num:Int) : Observable<HomeBean>



}