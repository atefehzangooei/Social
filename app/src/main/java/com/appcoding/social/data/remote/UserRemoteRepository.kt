package com.appcoding.social.data.remote

import com.appcoding.social.data.api.UserApi
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

class UserRemoteRepository(private val api : UserApi) {

    suspend fun signUp(signupRequest: SignupRequest) : StringMessage{
        return api.signUp(signupRequest)
    }

    suspend fun signIn(signinRequest: SigninRequest) : Response<SigninResponse>{
        return api.signIn(signinRequest)
    }

    suspend fun forgetPassword(forgetRequest: ForgetRequest) : StringMessage{
        return api.forgetPassword(forgetRequest)
    }

    suspend fun getUserInfo(userId : Long) : UserInfo?{
        return api.getUserInfo(userId)
    }

}