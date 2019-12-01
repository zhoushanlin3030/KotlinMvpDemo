package com.example.zsl.net

/**
 * 请求响应基类
 */
class BaseResponse<T>(val code: Int, val msg: String, val data: T) : BaseModel