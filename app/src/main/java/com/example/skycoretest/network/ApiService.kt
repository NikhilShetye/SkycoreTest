package com.artium.app.network

import com.example.skycoretest.dto.BusinessModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    fun listRepos(@Query("location") location: String?, @Query("term") term: String?): Call<BusinessModel>
}