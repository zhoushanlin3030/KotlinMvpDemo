package com.example.zsl.utils

import android.os.Environment
import com.example.zsl.api.UrlConstant
import java.io.File

/**
 * Routing
 * Desc TODO
 * Source
 * Created by zsl on 2019/12/7 15:54.
 * Modify by zsl on 2019/12/7 15:54.
 * Version 1.0
 */
object FileUtil {

    fun getCrashPath() : String{
        val file = File(getSdFolder(),UrlConstant.ROOT_FILE)
        if (!file.exists()){
            file.mkdir()
        }
        val crashFile = File(file,UrlConstant.CRASH_FILE)
        if (!crashFile.exists()){
            crashFile.mkdir()
        }
        return crashFile.path
    }

    private fun getSdFolder() : File{
        return if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Environment.getExternalStorageDirectory()
        } else {
            Environment.getDataDirectory()
        }
    }
}