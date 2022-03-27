package com.artium.app.network

import com.artium.app.ArtiumApp
import com.artium.app.BuildConfig
import com.artium.app.helper.UnsafeOkHttpClient
import com.artium.app.util.Constant
import com.better.stetho.RetroUtil
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/***
 *
 * @property artiumApp ArtiumApp
 * @property unsafeOkHttpClient UnsafeOkHttpClient
 * @property retrofit Retrofit
 * @property apiService ApiService
 * @constructor
 */
@Singleton
class RetrofitBuilder @Inject constructor(
    private val artiumApp: ArtiumApp,
    private val unsafeOkHttpClient: UnsafeOkHttpClient
) {

    private lateinit var retrofit: Retrofit
    lateinit var apiService: ApiService

    init {
        initRetroFitWithBaseUrl(artiumApp.getBaseUrl())
    }

    /**
     *
     * @param appBaseUrl AppUrl
     */
    fun initRetroFitWithBaseUrl(appBaseUrl: String) {
        val okHttpBuilder = RetroUtil.getOkHttpBuilder().let {
            if (BuildConfig.DEBUG) {
                it.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                it.readTimeout(60,TimeUnit.SECONDS)
                it.connectTimeout(60,TimeUnit.SECONDS)
            }
            unsafeOkHttpClient.getUnsafeOkHttpClient(it)
            //unsafeOkHttpClient.setCertificates(it)
        }

        okHttpBuilder.addInterceptor {
            val builder = it.request().newBuilder()
            //val requestUrl = URLDecoder.decode(request.url.toString(), "UTF-8")
            artiumApp.artiumSp.getAuthToken()?.let { authToken ->
                builder.addHeader(Constant.KEY_HEADER_AUTH, authToken)
            }
            it.proceed(builder.build())
        }

        retrofit = Retrofit.Builder()
            .baseUrl(appBaseUrl)
            //.addConverterFactory(nullOnEmptyConverterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build() //Doesn't require the adapter

        apiService = retrofit.create(ApiService::class.java)
    }


    //val apiService: ApiService = retrofit.create(ApiService::class.java)
}
// javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
// https://developer.android.com/training/articles/security-ssl


/*
private fun nullOnEmptyConverterFactory(): Converter.Factory {
    return object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ) = object :
            Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter =
                retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

            override fun convert(value: ResponseBody) =
                if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
        }
    }
}*/

/* to call logout if status code is 401 from server
val response = it.proceed(builder.build())
           if (response.code == 401) {
               val jsonObject = JsonObject()
               jsonObject.addProperty("userId", artiumApp.artiumSp.getUserId())
               apiService.logoutUser(jsonObject)
           }
           response*/
