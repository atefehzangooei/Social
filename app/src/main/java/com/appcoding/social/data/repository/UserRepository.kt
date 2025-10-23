package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.UserRemoteRepository
import com.appcoding.social.models.ForgetRequest
import com.appcoding.social.models.SigninRequest
import com.appcoding.social.models.SigninResponse
import com.appcoding.social.models.SignupRequest
import com.appcoding.social.models.StringMessage
import com.appcoding.social.models.UserInfo
import retrofit2.Response

class UserRepository(private val remoteRepository: UserRemoteRepository) {

    suspend fun signUp(signupRequest: SignupRequest) : StringMessage {
        return remoteRepository.signUp(signupRequest)
    }

    suspend fun signIn(signinRequest: SigninRequest) : Response<SigninResponse> {
        return remoteRepository.signIn(signinRequest)
    }

    suspend fun forgetPassword(forgetRequest: ForgetRequest) : StringMessage {
        return remoteRepository.forgetPassword(forgetRequest)
    }

    suspend fun getUserInfo(userId : Long) : UserInfo?{
        return remoteRepository.getUserInfo(userId)
    }
}