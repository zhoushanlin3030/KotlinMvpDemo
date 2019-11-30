package com.example.zsl

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates

class MyApplication : Application() {

    private var mRefWatcher : RefWatcher ?= null

    companion object {

        var context : Context by Delegates.notNull()
        private set

        fun getRefWatcher(context : Context) : RefWatcher?{
            val myApplication = context.applicationContext as MyApplication
            return myApplication.mRefWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        mRefWatcher = setupLeakCanary()
        initLog()
    }

    private fun setupLeakCanary(): RefWatcher{
        return if (LeakCanary.isInAnalyzerProcess(this)){
            RefWatcher.DISABLED
        } else {
            LeakCanary.install(this)
        }
    }

    private fun initLog(){
        PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(2)
            .methodOffset(7)
            .tag("zsl")
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}