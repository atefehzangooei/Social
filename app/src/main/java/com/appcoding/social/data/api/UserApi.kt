package com.appcoding.social.data.api

import com.appcoding.social.models.ForgetRequest
import com.appcoding.social.models.SigninRequest
import com.appcoding.social.models.SigninResponse
import com.appcoding.social.models.SignupRequest
import com.appcoding.social.models.StringMessage
import com.appcoding.social.models.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("users/signup")
    suspend fun signUp(@Body signupRequest: SignupRequest) : StringMessage

    @POST("users/signin")
    suspend fun signIn(@Body signinRequest: SigninRequest) : Response<SigninResponse>

    @POST("users/forgetpassword")
    suspend fun forgetPassword(@Body forgetRequest: ForgetRequest) : StringMessage

    @GET("users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId : Long) : UserInfo?


}