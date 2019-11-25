package com.example.zsl.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

object RetrofiManager {

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

   private fun addHeaderInterceptor() : Interceptor {
      return Interceptor {
         val originalReq = it.request()
         val newRequest = originalReq.newBuilder()
            .header("token","")
            .method(originalReq.method(),originalReq.body())
            .build()
         it.proceed(newRequest)
      }
   }

}
