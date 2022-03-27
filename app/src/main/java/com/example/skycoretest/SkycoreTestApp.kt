package com.example.skycoretest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SkycoreTestApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    fun getBaseUrl() = "https://api.yelp.com/v3/businesses/"
    fun getAuthToken() = "XPFgzKwZGK1yqRxHi0d5xsARFOLpXIvccQj5jekqTnysweGyoIfVUHcH2tPfGq5Oc9kwKHPkcOjk2d1Xobn7aTjOFeop8x41IUfVvg2Y27KiINjYPADcE7Qza0RkX3Yx"
}