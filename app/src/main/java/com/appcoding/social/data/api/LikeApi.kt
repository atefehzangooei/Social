package com.appcoding.social.data.api

import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.StringMessage
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeApi {

    @POST("likes")
    suspend fun likePost(@Body likeRequest : LikeRequest) : StringMessage

    @DELETE("likes/dislike/{postId}/{userId}")
    suspend fun disLikePost(@Path("postId") postId: Long,
                            @Path("userId") userId : Long)

}