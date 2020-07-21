package com.business.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created by claire on 2020/7/21.
 */
class SunnyWeatherApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = "4nvILiFVFHOOmEQR"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}