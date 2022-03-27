package com.example.skycoretest

import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var artiumApp: SkycoreTestApp

    open fun showLoading() {

    }

    open fun hideLoading() {

    }

}