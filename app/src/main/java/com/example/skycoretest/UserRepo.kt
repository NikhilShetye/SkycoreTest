package com.artium.app.service.repo

import com.artium.app.network.Receptor
import com.artium.app.network.RetrofitBuilder
import com.artium.app.network.Status
import com.artium.app.service.model.*
import com.artium.app.ui.viewmodel.dto.OtpLoginRequestDto
import com.artium.app.util.ArtiumSp
import com.artium.app.util.Constant
import com.artium.app.util.Logs
import com.artium.utils.extensions.exhaustive
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val artiumSp: ArtiumSp,
    private val retrofitBuilder: RetrofitBuilder
) {

    /**
     *
     * @return Receptor<UserLoginResponse>
     */
    fun loginUser(login: String, password: String) =
        Receptor.Builder<LoginResponse>().apply {
            try {

                val jsonObject = JsonObject()
                jsonObject.addProperty("username", login)
                jsonObject.addProperty("password", password)

                retrofitBuilder.apiService.loginUser(jsonObject).execute().let { apiResponse ->
                    if (apiResponse.isSuccessful) {
                        try {
                            artiumSp.saveAuthToken(apiResponse.headers()[Constant.KEY_HEADER_AUTH])
                        } catch (e: Exception) {
                            Logs.e(
                                javaClass.simpleName,
                                "loginUser Authorization headers exception",
                                e
                            )
                        }
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
                Logs.e(javaClass.simpleName, "loginUser() builder: $this")
            }
        }.build()


    fun otplogin(code: String?, mobileNo: String?, otp: String?, emailId: String?) =
        Receptor.Builder<LoginResponse>().apply {
            try {
                retrofitBuilder.apiService.otplogin(
                    OtpLoginRequestDto(
                        code,
                        mobileNo,
                        otp,
                        emailId
                    )
                ).execute().let { apiResponse ->
                    if (apiResponse.isSuccessful) {
                        try {
                            artiumSp.saveAuthToken(apiResponse.headers()[Constant.KEY_HEADER_AUTH])
                        } catch (e: Exception) {
                            Logs.e(
                                javaClass.simpleName,
                                "otplogin Authorization headers exception",
                                e
                            )
                        }
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
                Logs.e(javaClass.simpleName, "otplogin() builder: $this")
            }
        }.build()

    /*suspend fun loginByOtpVerify(otp: String) =
        Receptor.Builder<LoginResponse>().apply {
            try {


                retrofitBuilder.apiService.loginByOtpVerify(otp).execute().let { apiResponse ->
                    if (apiResponse.isSuccessful) {
                        try {
                            artiumSp.saveAuthToken(apiResponse.headers()[Constant.KEY_HEADER_AUTH])
                        } catch (e: Exception) {
                            Logs.e(
                                javaClass.simpleName,
                                "loginByOtpVerify Authorization headers exception",
                                e
                            )
                        }
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
                Logs.e(javaClass.simpleName, "loginByOtpVerify() builder: $this")
            }
        }.build()*/

    /**
     *
     * @param loginUserId Int
     * @return Receptor<LogoutResponse>
     */
    fun logoutUser(loginUserId: Int) = Receptor.Builder<LogoutResponse>().apply {
        try {
            val jsonObject = JsonObject()
            jsonObject.addProperty("userId", loginUserId)

            retrofitBuilder.apiService.logoutUser(jsonObject).execute().let { apiResponse ->
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
            Logs.e(javaClass.simpleName, "logoutUser() builder: $this")
        }
    }.build()

    /**
     *
     * @param loginUserId Int
     * @return Receptor<ProfileResponse>
     */
    fun getUserProfile(loginUserId: Int) = Receptor.Builder<ProfileResponse>().apply {
        try {
            retrofitBuilder.apiService.getUserProfile(loginUserId).execute().let { apiResponse ->
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { profileResponse ->
                        status = Status.SUCCESS
                        response = profileResponse
                    } ?: run {
                        status = Status.EXCEPTION
                        throwable = NullPointerException()
                        errorMsg = throwable?.message
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
            Logs.e(javaClass.simpleName, "getUserProfile() builder: $this")
        }
    }.build()

    fun validateNewUser(countryCode: String, mobileNo: String) =
        Receptor.Builder<String?>().apply {
            try {
                retrofitBuilder.apiService.validateNewUser(countryCode, mobileNo).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { validateNewUserResponse ->
                                status = Status.SUCCESS
                                response = validateNewUserResponse
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
                Logs.e(javaClass.simpleName, "validateNewUser() builder: $this", e)
            }
        }.build()

    fun ValidateNewUser1(emailId: String) =
        Receptor.Builder<String?>().apply {
            try {
                retrofitBuilder.apiService.validateNewUser1(emailId).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { validateNewUser1Response ->
                                status = Status.SUCCESS
                                response = validateNewUser1Response
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
                Logs.e(javaClass.simpleName, "ValidateNewUser1() builder: $this", e)
            }
        }.build()

    fun forgotPassword(mobileNo: String) =
        Receptor.Builder<ForgotPasswordResponse?>().apply {
            try {
                retrofitBuilder.apiService.forgotPassword(mobileNo).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { forgotPasswordResponse ->
                                status = Status.SUCCESS
                                response = forgotPasswordResponse
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
                Logs.e(javaClass.simpleName, "forgotPassword() builder: $this", e)
            }
        }.build()

    fun verifyOtpFP(otp: String) =
        Receptor.Builder<VerifyOtpFPResponse?>().apply {
            try {
                retrofitBuilder.apiService.verifyOtpFP(otp).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { verifyOtpFPResponse ->
                                status = Status.SUCCESS
                                response = verifyOtpFPResponse
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
                Logs.e(javaClass.simpleName, "verifyOtpFP() builder: $this", e)
            }
        }.build()

    fun savePassword(
        jsonObject: JsonObject
    ) =
        Receptor.Builder<SavePasswordResponse>().apply {
            try {
                retrofitBuilder.apiService.savePassword(
                    jsonObject
                ).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { savePasswordResponse ->
                                status = Status.SUCCESS
                                response = savePasswordResponse
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
                Logs.e(javaClass.simpleName, "savePassword() builder: $this", e)
            }
        }.build()

    fun studentbillingdetails(studentId: String, courseId: String) =
        Receptor.Builder<List<StudentBillingDetResponse?>>().apply {
            try {
                retrofitBuilder.apiService.studentbillingdetails(studentId, courseId).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { studentBillingDetResponse ->
                                status = Status.SUCCESS
                                response = studentBillingDetResponse
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
                Logs.e(javaClass.simpleName, "studentbillingdetails() builder: $this", e)

            }
        }.build()

    fun courseRemainingCount(studentId: String, courseId: String) =
        Receptor.Builder<List<StudentBillingDetResponse>>().apply {
            try {
                retrofitBuilder.apiService.courseRemainingCount(studentId).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { studentBillingDetResponse ->
                                status = Status.SUCCESS
                                response = studentBillingDetResponse
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
                Logs.e(javaClass.simpleName, "courseRemainingCount() builder: $this", e)

            }
        }.build()

    fun interestTags() =
        Receptor.Builder<List<InterestTag>>().apply {
            try {
                retrofitBuilder.apiService.interestTags().execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { interestTagsRes ->
                                status = Status.SUCCESS
                                response = interestTagsRes
                            } ?: run {
                                status = Status.EXCEPTION
                                throwable = NullPointerException()
                                errorMsg = throwable?.stackTraceToString()
                                errorCode = apiResponse.code()
                            }
                        } else {
                            status = Status.ERROR
                            errorMsg = apiResponse.errorBody()?.string()
                            errorCode = apiResponse.code()
                        }
                    }.exhaustive
            } catch (e: Exception) {
                status = Status.EXCEPTION
                throwable = e
                errorMsg = throwable?.message
                Logs.e(javaClass.simpleName, "interestTags() builder: $this", e)
            }

        }

    fun profile(profilePhoto:String,uId:String,interestTags: List<String>) =
        Receptor.Builder<String>().apply {
            try {
                retrofitBuilder.apiService.profile(UpdateProfileModel(profilePhoto,uId,interestTags)).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { prodfileRes ->
                                status = Status.SUCCESS
                                response = prodfileRes
                            } ?: run {
                                status = Status.EXCEPTION
                                throwable = NullPointerException()
                                errorMsg = throwable?.stackTraceToString()
                                errorCode = apiResponse.code()
                            }
                        } else {
                            status = Status.ERROR
                            errorMsg = apiResponse.errorBody()?.string()
                            errorCode = apiResponse.code()
                        }
                    }.exhaustive
            } catch (e: Exception) {
                status = Status.EXCEPTION
                throwable = e
                errorMsg = throwable?.message
                Logs.e(javaClass.simpleName, "profile() builder: $this", e)
            }
        }

    fun uploadProfilePhoto(credentialId: String, part: MultipartBody.Part) =
        Receptor.Builder<UploadedProfilePhotoModel>().apply {
            try {
                retrofitBuilder.apiService.uploadProfilePhoto(credentialId, part).execute()
                    .let { apiResponse ->
                        if (apiResponse.isSuccessful) {
                            apiResponse.body()?.let { fileUploadedRes ->
                                status = Status.SUCCESS
                                response = fileUploadedRes
                            } ?: run {
                                status = Status.EXCEPTION
                                throwable = NullPointerException()
                                errorMsg = throwable?.stackTraceToString()
                                errorCode = apiResponse.code()
                            }
                        } else {
                            status = Status.ERROR
                            errorMsg = apiResponse.errorBody()?.string()
                            errorCode = apiResponse.code()
                        }
                    }.exhaustive
            } catch (e: Exception) {
                status = Status.EXCEPTION
                throwable = e
                errorMsg = throwable?.message
            }
        }.build()
}