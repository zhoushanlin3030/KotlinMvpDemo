package com.example.zsl.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/7 12:17.
 * Modify by zsl on 2019/12/7 12:17.
 * Version 1.0
 */
class CrashLogUtil private constructor(): Thread.UncaughtExceptionHandler {

    private val mFormatter by lazy {
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.ENGLISH)
    }

    private var mContext : Context ?= null

    companion object {
        private val defaultHandler: Thread.UncaughtExceptionHandler by lazy {
            Thread.getDefaultUncaughtExceptionHandler()
        }
        private val appInfo : HashMap<String,String> by lazy {
            HashMap<String,String>()
        }

        val instance : CrashLogUtil by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
            CrashLogUtil()
        }
    }

    fun init(context: Context){
        mContext = context
        Thread.setDefaultUncaughtExceptionHandler(defaultHandler)
    }

    override fun uncaughtException(p0: Thread?, p1: Throwable?) {
        if (!handleException(p1)){
            defaultHandler.uncaughtException(p0,p1)
        } else {
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 处理异常
     */
    private fun handleException(ex : Throwable?) : Boolean{
        mContext?.let { collectDeviceInfo(it) }
        ex?.let { saveToFile(it) }
        return true
    }


    private fun collectDeviceInfo(context: Context){
        try {
            val pm = context.packageManager
            val info = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            if (info != null) {
                info.versionName ?.let {
                    appInfo.put("versionName",info.versionName)
                }
                appInfo["versionCode"] = info.versionCode.toString()
            }
        } catch (e:Exception){
            Logger.d("zsl",e.message)
        }

        val fields = Build::class.java.declaredFields
        for (f in fields){
            try {
                f.isAccessible = true
                appInfo[f.name] = f[null].toString()
            } catch (e : Exception) {
                Logger.d("zsl",e.message)
            }
        }
    }

    private fun saveToFile(tx : Throwable){
        val b = StringBuilder()
        for (info in appInfo){
            b.append(info.key+"="+info.value+"\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        tx.printStackTrace(printWriter)
        var cause = tx.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        b.append(result)
        var fos: FileOutputStream ?= null

        try {
            val millis = System.currentTimeMillis()
            val time = mFormatter.format(Date())
            val fileName = "zsl-$time-$millis.txt"
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                val crashPath = FileUtil.getCrashPath()
                val filePath  = crashPath + File.separator + fileName
                fos = FileOutputStream(filePath)
                fos.write(b.toString().toByteArray())
                val uri = Uri.fromFile(File(filePath))
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri)
                mContext?.sendBroadcast(intent)
                //TODO 上传服务器
            }
        } catch (e:Exception){
            Logger.d("zsl",e.message)
        } finally {
            try {
                fos ?.let { fos.close() }
            } catch (e : Exception){
                Logger.d("zsl",e.message)
            }
        }
    }

}
