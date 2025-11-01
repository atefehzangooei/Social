package com.appcoding.social.data.api

import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StringMessage
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface SavePostApi {

    @POST("save_post")
    suspend fun savePost(@Body savePostRequest : SavePostRequest) : StringMessage

    @DELETE("save_post/{postId}/{userId}")
    suspend fun unSavePost(@Path("postId") postId: Long,
                           @Path("userId") userId : Long) : StringMessage

}