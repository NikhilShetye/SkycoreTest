package com.artium.app.network

import com.artium.app.PaidFeedback
import com.artium.app.config.AppConfiguration
import com.artium.app.service.model.*
import com.artium.app.ui.viewmodel.dto.*
import com.artium.app.ui.viewmodel.dto.configdto.AppConfigModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //@GET("course/studentclasses/{studentId}?classStatus=class_scheduled&page=0&size=1&isRequiredForToday=true")
    /* To Test Classes only */
    @GET("course/studentclasses/{studentId}?page=0&size=500")
    fun getStudentScheduledClassInfo(
        @Path("studentId") studentId: Int,
        @Query("isRequiredForToday") isRequiredForToday: Boolean,
        @Query("classStatus") classStatus: String,
    ): Call<StudentClassResponse?>

    @GET("course/studentclasses/unscheduled/{credential_id}")
    fun getStudentUnscheduledClassInfo(
        @Path("credential_id") credential_id: String,
        @Query("studentId") studentId: String,
        @Query("courseId") courseId: String,
        @Query("size") size: Int = 500,
        @Query("page") page: Int = 0,
    ): Call<StudentClassResponse?>

    @POST("course/feedback/givefeedback")
    fun getStudentFeedbackInfo(
        @Body jsonObject: JsonObject,
    ): Call<String?>

    @POST("auth/login")
    fun loginUser(@Body loginJson: JsonObject): Call<LoginResponse?>

    @POST("auth/user/logout")
    fun logoutUser(@Body logoutJson: JsonObject): Call<LogoutResponse?>

    @GET("users/student/{studentId}/profileAPI")
    fun getUserProfile(@Path("studentId") studentId: Int): Call<ProfileResponse?>

    @POST("auth/user/otplogin")
    fun otplogin(@Body loginJson: OtpLoginRequestDto): Call<LoginResponse?>

    //http://localhost:8002/user/loginByOtpVerify
    /*@POST("user/loginByOtpVerify")
    fun loginByOtpVerify(@Query("otp") otp:String): Call<LoginResponse?>*/

    //https://dev.artiumacademy.com:8765/api/course/getLatestTeacherProfile/580/22
    @GET("course/getLatestTeacherProfile/{studentId}/{course_id}")
    fun getLatestTeacherProfile(
        @Path("studentId") studentId: Int,
        @Path("course_id") course_id: Int
    ): Call<LatestTeacherProfileDto>

    //https://dev.artiumacademy.com:8765/api/course/loadClassJoiningDetails/169/2838?status=book
    @GET("course/loadClassJoiningDetails/{userId}/{classSchedule_id}")
    fun loadClassJoiningDetails(
        @Path("userId") userId: String,
        @Path("classSchedule_id") classSchedule_id: String,
        @Query("status") status: String
    ): Call<JoinMeetingDto>

    //https://dev.artiumacademy.com:8765/api/course/getZoomMeetingStatus/2501
    @GET("course/getZoomMeetingStatus/{classSchedule_id}")
    fun getZoomMeetingStatus(@Path("classSchedule_id") classSchedule_id: String): Call<String>

    //    https://dev.artiumacademy.com:8765/api/course/showcourseamount/770
    @GET("course/showcourseamount/{studentId}")
    fun showcourseamount(
        @Path("studentId") studentId: String,
    ): Call<List<ShowcourseamountResponse>>

    //    https://dev.artiumacademy.com:8765/api/StudRegister/subscribePlanInZoho
    @POST("StudRegister/subscribePlanInZoho")
    fun subscribePlanInZoho(
        @Body jsonObject: JsonObject
    ): Call<SubScribePlanInZohoRes>

    //    https://dev.artiumacademy.com:8765/api/course/recommendedCourses/457
    @GET("course/recommendedCourses/{studentId}")
    fun recommendedCourses(
        @Path("studentId") studentId: String,
    ): Call<List<ReccommendedCourseRes>>

    @GET("users/interestTags")
    fun interestTags(): Call<List<InterestTag>>

    @PUT("users/profile")
    fun profile(@Body jsonObject: UpdateProfileModel): Call<String>

    @Multipart
    @POST("users/upload/profilephoto")
    fun uploadProfilePhoto(
        @Query("credentialId") credentialId: String,
        @Part partBody: MultipartBody.Part
    ): Call<UploadedProfilePhotoModel>

    @GET("course/feedbacks")
    fun feedbacks(
        @Query("classId") classId: String,
        @Query("teacherId") teacherId: String,
    ): Call<List<PaidFeedback>>


    //Book Demo Apis

    //    https://dev.artiumacademy.com:8765/api/users/tags?parentType=vocal
    @GET("users/tags")
    fun getTags(@Query("parentType") parentType: String): Call<List<TagsResponse>?>

    //    https://dev.artiumacademy.com:8765/api/gupshup/sendOtp/+91/9769656387
    @GET("gupshup/sendOtp/{countryCode}/{mobileNo}")
    fun sendOtp(
        @Path("countryCode") countryCode: String,
        @Path("mobileNo") mobileNo: String
    ): Call<SendOtpResponse?>

    //called in place of sendotp
//    Request URL: https://dev.artiumacademy.com:8765/api/users/user/sendloginotp?mobileNo=9146505566
    @POST("users/user/sendloginotp")
    fun sendloginotp(
        @Query("mobileNo") mobileNo: String? = null,
        @Query("emailId") emailId: String? = null
    ): Call<String?>

    //    https://dev.artiumacademy.com:8765/api/gupshup/verifyOtp/+91/9619381977/2126
    @GET("gupshup/verifyOtp/{countryCode}/{mobileNo}/{otp}")
    fun verifyOtp(
        @Path("countryCode") countryCode: String,
        @Path("mobileNo") mobileNo: String,
        @Path("otp") otp: String
    ): Call<VerifyOtpResponse?>


    //    https://dev.artiumacademy.com:8765/api/users/agegroups
    @GET("users/agegroups")
    fun agegroups(): Call<List<Agegroupmodel?>>


    //    https://dev.artiumacademy.com:8765/api/users/languages
    @GET("users/languages/{parentTagId}")
    fun languages(@Path("parentTagId") parentTagId: String): Call<List<LanguagesResponse>>

    //    https://dev.artiumacademy.com:8765/api/users/timezones
    @GET("users/timezones")
    fun timezones(): Call<List<TimezonesResponse?>>

    //    https://dev.artiumacademy.com:8765/api/users/addStudent
    @POST("users/addStudent")
    fun addStudent(@Body addStudentJson: JsonObject): Call<AddStudentResponse?>

    //    https://dev.artiumacademy.com:8765/api/course/listFreeSlots/276
    @GET("course/listFreeSlots/{credentialId}")
    fun listFreeSlots(@Path("credentialId") credentialId: String): Call<String?>

    //    https://dev.artiumacademy.com:8765/api/course/allocateTeacherForBooking
    @POST("course/allocateTeacherForBooking/")
    fun allocateTeacher(
        @Body allocateTeacherForJson: JsonObject,
        @Query("status") status: String
    ): Call<String?>

    @POST("course/allocateTeacherForBooking/")
    fun allocateTeacherForTrialClass(
        @Body allocateTeacherForJson: TrialSlotBookingAllocateTeacherRequest,
        @Query("status") status: String
    ): Call<TrialSlotBookingAllocateTeacherResponse>

    //    https://dev.artiumacademy.com:8765/api/course/scheduleDemoClass
    @POST("course/scheduleDemoClass")
    fun scheduleDemoClass(@Body scheduleDemoClassJson: JsonObject): Call<ScheduleDemoClassResponse?>

    //    https://dev.artiumacademy.com:8765/api/email/sendEmail
    @POST("email/sendEmail/")
    fun sendEmail(@Body sendEmailJson: JsonObject): Call<String?>

    //    https://dev.artiumacademy.com:8765/api/users/updateTag/191/7
    @POST("users/updateTag/{student_id}/{interest_id}")
    fun updateTag(
        @Path("student_id") student_id: String,
        @Path("interest_id") interest_id: String
    ): Call<String?>

    //    https://dev.artiumacademy.com:8765/api/users/updateLanguage/106/2
    @POST("users/updateLanguage/{student_id}/{language_id}")
    fun updateLanguage(
        @Path("student_id") student_id: String, @Path("language_id") language_id
        : String
    ): Call<String?>

    @GET(value = "http://ipwhois.pro/json/?key=sza7zAulrNigPMoM")
    fun fetchSystemZoneInfo(): Call<IpWhoDto>

    @GET(value = AppConfiguration.LMS_URL + "/{courseId}")
    fun getLearningDetails(@Path("courseId") courseId: String): Call<List<LearningDetailsDto>>

    @GET()
    fun getCourseLearningDetails(@Url url: String): Call<String?>

    //    curl --location --request GET 'https://dev.artiumacademy.com:8765/api/users/getCountry'
    @GET("users/getCountry")
    fun getCountryList(): Call<List<CountryDto>>

    //    curl --location --request GET 'https://dev.artiumacademy.com:8765/api/course/noshowclass/1378'
    @POST("course/noshowclass/{classSchedule_id}/{studentType}")
    fun noshowclass(
        @Path("classSchedule_id") classSchedule_id: String,
        @Path("studentType") studentType: String
    ): Call<String?>

    //    curl --location --request GET 'https://dev.artiumacademy.com:8765/api/course/isResheduleAllowedForTrail/580'
    @GET("course/isResheduleAllowedForTrail/{student_id}")
    fun isResheduleAllowedForTrail(
        @Path("student_id") student_id: String,
        @Query("isNoShow") isNoShow: Boolean
    ): Call<RescheduleDetailDto>

    //    curl --location --request GET 'https://dev.artiumacademy.com:8765/api/course/changeStatusofClassforNoShowTrail/1147'
    @GET("course/changeStatusofClassforNoShowTrail/{classSchedule_id}")
    fun changeStatusofClassforNoShowTrail(@Path("classSchedule_id") classSchedule_id: String): Call<String?>

    //studentVerify apis

    //    URL : https://182.72.129.214:8090/api/users/user/studentVerifySendOTP?mobileNo=9561750871
    @GET("users/user/studentVerifySendOTP")
    fun studentVerifySendOTP(
        @Query("mobileNo") mobileNo: String
    ): Call<String?>

    //URL : https://182.72.129.214:8090/api/users/user/studentVerifyOTP?otp=1604&credentialId=2
    @GET("users/user/studentVerifyOTP")
    fun studentVerifyOTP(
        @Query("otp") otp: String,
        @Query("credentialId") credentialId: String,
    ): Call<String?>

    //    URL : https://182.72.129.214:8090/api/users/updateMobileNo?mobileNo=9167347735&credentailId=2
    @GET("users/updateMobileNo")
    fun updateMobileNo(
        @Query("mobileNo") mobileNo: String,
        @Query("credentailId") credentailId: String,
    ): Call<String?>

    //studentVerify apis end


    //Schedule, Reschedule and Cancel Class Apis
//    https://dev.artiumacademy.com:8765/api/course/teacherslotdetails/student/356?courseId=3&fromDate=03/05/2021&toDate=10/05/2021
    @GET("course/teacherslotdetails/student/{studentId}")
    fun teacherSlotDetails(
        @Path("studentId") studentId: String,
        @Query("courseId") courseId: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
    ): Call<List<TeacherSlotDetailsResponse?>>

    //    https://dev.artiumacademy.com:8765/api/course/courseListForStudentDashboard/273
    @GET("course/courseListForStudentDashboard/{studentId}")
    fun courseListForStudent(@Path("studentId") studentId: String):
            Call<List<CourseListForStudentResponse?>>

    @POST("course/rescheduleClass/{classScheduleId}")
    fun rescheduleClass(
        @Path("classScheduleId") classScheduleId: Int?,
        @Body rescheduleClassJson: JsonObject,
        @Query("status") status: String
    ): Call<String?>

    @POST("course/scheduleClass")
    fun scheduleClass(
        @Body scheduleClassJson: JsonObject,
        @Query("status") status: String
    ): Call<String?>

    @POST("course/cancelClass/{classScheduleId}")
    fun cancelClass(@Path("classScheduleId") classScheduleId: Int?): Call<CancelClassDto>

    //https://dev.artiumacademy.com:8765/api/course/showstudentclassanddescription
    @POST("course/showstudentclassanddescription")
    fun showstudentclassanddescription(@Body jsonObject: JsonObject): Call<String?>

    //    https://dev.artiumacademy.com:8765/api/course/class_cancellation_reasons/student
    @GET("course/class_cancellation_reasons/student")
    fun class_cancellation_reasons():
            Call<List<CancelReasonResponse?>>

    //    https://dev.artiumacademy.com:8765/api/users/status/exercise?courseId=25&classId=6&exerciseNumber=12&studentId=271
    @GET("users/status/exercise")
    fun exercisestatuschange(
        @Query("courseId") courseId: String,
        @Query("classId") classId: String,
//                             @Query("exerciseNumber") exerciseNumber: String,
        @Query("studentId") studentId: String
    ): Call<List<ExerciseStatusModel>>
//    https://dev.artiumacademy.com:8765/api/users/exercise

    @POST("users/exercise")
    fun sendexercisestatus(@Body exerciseSatuses: List<ExerciseStatusModel>): Call<String>

    //    https://dev.artiumacademy.com:8765/api/course/class_schedule/1000/cancelreason_by_student/1127?cancellationReasonByStudent=reason
    @POST("course/class_schedule/{class_schedule_id}/cancelreason_by_student/{credential_id}")
    fun cancelReasonByStudent(
        @Path("class_schedule_id") class_schedule_id: String,
        @Path("credential_id") credential_id: String,
        @Query("cancellationReasonByStudent") cancellationReasonByStudent: String
    ): Call<CancelReasonDto>


    //forgot password apis
//    https://dev.artiumacademy.com:8765/api/users/ValidateNewUser/+91/8888888888
    @GET("users/ValidateNewUser/{countryCode}/{mobileNo}")
    fun validateNewUser(
        @Path("countryCode") countryCode: String,
        @Path("mobileNo") mobileNo: String
    ): Call<String?>

    //   https://dev.artiumacademy.com:8765/api/user/ValidateNewUser1/sonamsal9119@gmail.com
    @GET("users/ValidateNewUser1/{emailId}")
    fun validateNewUser1(@Path("emailId") emailId: String): Call<String?>

    //    https://dev.artiumacademy.com:8765/api/users/user/forgotPassword?mobileNo=9619381977
    @GET("users/user/forgotPassword")
    fun forgotPassword(@Query("mobileNo") mobileNo: String): Call<ForgotPasswordResponse?>

    //    https://dev.artiumacademy.com:8765/api/users/user/verifyOTP?otp=9608
    @GET("users/user/verifyOTP")
    fun verifyOtpFP(@Query("otp") otp: String): Call<VerifyOtpFPResponse?>

    //    https://dev.artiumacademy.com:8765/api/users/user/savePassword
    @POST("users/user/savePassword")
    fun savePassword(@Body savePasswordJson: JsonObject): Call<SavePasswordResponse?>

    //billing apis
//    https://dev.artiumacademy.com:8765/api/users/studentbillingdetails/91/15
    @GET("users/studentbillingdetails/{studentId}/{courseId}")
    fun studentbillingdetails(
        @Path("studentId") studentId: String,
        @Path("courseId") courseId: String
    ): Call<List<StudentBillingDetResponse?>>

    //    https://dev.artiumacademy.com:8765/api/users/courseRemainingCount/457/12
    @GET("users/courseRemainingCount/{studentId}")
    fun courseRemainingCount(@Path("studentId") studentId: String): Call<List<StudentBillingDetResponse>>

    //    https://teach.artiumacademy.com/webservice/rest/server.php?courseid=2&amp;wstoken=9f8047f40983381e9592845f4552f7df&amp;moodlewsrestformat=json&amp;wsfunction=local_custom_service_get_session_content&amp;classid=1&amp;classdata=true&amp;techerrecomendeddata=true

    @GET()
    fun getSessionContent(@Url url: String): Call<List<ClassContentResponse>>

    @GET()
    fun getAppConfig(@Url url: String): Call<AppConfigModel>
}