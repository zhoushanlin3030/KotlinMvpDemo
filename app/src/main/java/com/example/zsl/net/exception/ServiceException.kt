package com.example.zsl.net.exception

import java.lang.RuntimeException

/**
 * 服务器返回异常
 */
class ServiceException : RuntimeException{

    var code : Int ?= null
    var msg : String ?= null

    constructor(message : String) : super(Throwable(message))

    constructor(code : Int, message: String) : super(Throwable(message)){
        this.code = code
        this.msg = message
    }
}