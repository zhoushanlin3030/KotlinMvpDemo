package com.example.zsl.net

import com.example.zsl.MyApplication
import com.example.zsl.api.ApiService
import com.example.zsl.api.UrlConstant
import com.example.zsl.utils.NetworkUtil
import com.example.zsl.utils.Preference
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofiManager {

    val service : ApiService by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
        getRetrofit().create(ApiService::class.java)
    }

   private var token:String by Preference("token","")


   /**
    * 设置公共参数
    */
   private fun addParameterInterceptor() : Interceptor {
      return Interceptor {
         val request:Request
         val originalReq = it.request()
         val modifyUrl = originalReq.url().newBuilder()
            .addQueryParameter("udid","d2807c895f0348a180148c9dfa6f2feeac0781b5")
            .addQueryParameter("deviceModel","")
            .build()
         request = originalReq.newBuilder().url(modifyUrl).build()
         it.proceed(request)
      }
   }

   /**
    * 设置请求头参数
    */
   private fun addHeaderInterceptor() : Interceptor {
      return Interceptor {
         val originalReq = it.request()
         val newRequest = originalReq.newBuilder()
            .header("token",token)
            .method(originalReq.method(),originalReq.body())
            .build()
         it.proceed(newRequest)
      }
   }

   /**
    * 缓存设置
    */
   private fun addCacheInterceptor() : Interceptor {
      return Interceptor {
         var request = it.request()
         if (!NetworkUtil.isNetworkAvailable(MyApplication.context)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
         }
         val response = it.proceed(request)
         if (NetworkUtil.isNetworkAvailable(MyApplication.context)){
            val maxAge = 0
            // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
            response.newBuilder()
                    .addHeader("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
         } else {
            // 无网络时，设置超时为4周  只对get有用,post没有缓冲
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("nyn")
                    .build()
         }
      }
   }

   /**
    * Retrofit 对象
    */
   private fun getRetrofit() : Retrofit {
      return Retrofit.Builder()
              .baseUrl(UrlConstant.BASE_URL)
              .client(getOkhttpClient())
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .addConverterFactory(GsonConverterFactory.create())
              .build()
   }

   private fun getOkhttpClient() : OkHttpClient {

      val httpLoggingInterceptor = HttpLoggingInterceptor()
      httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

      val cacheFile = File(MyApplication.context.cacheDir,"cache")
      val cache = Cache(cacheFile,1024 * 1024 * 50)

      return OkHttpClient.Builder()
              .addInterceptor(addParameterInterceptor())
              .addInterceptor(addHeaderInterceptor())
              .addInterceptor(addCacheInterceptor())
              .cache(cache)
              .writeTimeout(30L,TimeUnit.SECONDS)
              .readTimeout(30L,TimeUnit.SECONDS)
              .connectTimeout(30L,TimeUnit.SECONDS)
              .build()
   }

}
