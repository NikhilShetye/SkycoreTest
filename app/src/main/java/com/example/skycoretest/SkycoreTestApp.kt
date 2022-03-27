package com.artium.app

import android.app.Application
import com.artium.app.config.AppConfiguration
import com.artium.app.util.ArtiumSp
import com.artium.app.util.Constant
import com.better.stetho.RetroUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ArtiumApp : Application() {

    @Inject
    lateinit var artiumSp: ArtiumSp

    override fun onCreate() {
        super.onCreate()
        RetroUtil.initStethoAccordingly(this)
    }

    fun getBaseUrl() = AppConfiguration.API_BASE_URL

    fun getConfigUrl() = AppConfiguration.CONFIG_URL
}