package com.artium.app.base

import androidx.appcompat.app.AppCompatActivity
import com.artium.app.ArtiumApp
import com.artium.app.network.Receptor
import com.artium.app.service.model.ErrorResponse
import com.artium.app.util.Logs
import com.google.gson.GsonBuilder
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var artiumApp: ArtiumApp

    open fun showLoading() {

    }

    open fun hideLoading() {

    }

    /**
     *
     * @param receptor Receptor<*>
     * @return ErrorResponse?
     */
    fun getApiErrorResp(receptor: Receptor<*>): ErrorResponse? = try {
        GsonBuilder().create()
            .fromJson(receptor.getErrorMsg(), ErrorResponse::class.java)
    } catch (e: Exception) {
        Logs.e(javaClass.simpleName, "getApiErrorResp ${e.message}", e)
        null
    }
}