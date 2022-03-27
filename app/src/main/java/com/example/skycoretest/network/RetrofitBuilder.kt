package com.artium.app.network

import com.example.skycoretest.SkycoreTestApp
import com.example.skycoretest.UnsafeOkHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitBuilder @Inject constructor(
    private val artiumApp: SkycoreTestApp,
    private val unsafeOkHttpClient: UnsafeOkHttpClient
) {

    private lateinit var retrofit: Retrofit
    lateinit var apiService: ApiService

    init {
        initRetroFitWithBaseUrl(artiumApp.getBaseUrl())
    }

    private fun initRetroFitWithBaseUrl(appBaseUrl: String) {
        val okHttpBuilder = unsafeOkHttpClient.getUnsafeOkHttpClient(OkHttpClient().newBuilder())

        okHttpBuilder.addInterceptor {
            val builder = it.request().newBuilder()
            //val requestUrl = URLDecoder.decode(request.url.toString(), "UTF-8")
            artiumApp.getAuthToken().let { authToken ->
                builder.addHeader("Authorization", "Bearer ${artiumApp.getAuthToken()}")

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
}
