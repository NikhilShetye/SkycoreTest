package com.example.skycoretest

import com.artium.app.network.Receptor
import com.artium.app.network.RetrofitBuilder
import com.artium.app.network.Status
import com.example.skycoretest.dto.BusinessModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val artiumSp: SkycoreTestApp,
    private val retrofitBuilder: RetrofitBuilder
) {
    fun listRepos(location: String, term: String) =
        Receptor.Builder<BusinessModel>().apply {
            try {

                retrofitBuilder.apiService.listRepos(location,term).execute().let { apiResponse ->
                    if (apiResponse.isSuccessful) {
                        apiResponse.body()?.let { loginResponse ->
                            status = Status.SUCCESS
                            response = loginResponse
                        } ?: run {
                            status = Status.EXCEPTION
                            throwable = NullPointerException()
                            errorMsg = throwable?.message
                            errorCode = apiResponse.code()
                        }
                    } else {
                        status = Status.ERROR
                        errorCode = apiResponse.code()
                        errorMsg = apiResponse.errorBody()?.string()
                    }
                }
            } catch (e: Exception) {
                status = Status.EXCEPTION
                throwable = e
                errorMsg = throwable?.message
            }
        }.build()
}