package com.appcoding.social.data.remote

import com.appcoding.social.data.api.LikeApi
import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.StringMessage
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

class LikeRemoteRepository(private val api : LikeApi) {

    suspend fun likePost(likeRequest : LikeRequest) : StringMessage{
        return api.likePost(likeRequest)
    }

    suspend fun disLikePost(postId: Long,
                            userId : Long){
        return api.disLikePost(postId, userId)
    }

}