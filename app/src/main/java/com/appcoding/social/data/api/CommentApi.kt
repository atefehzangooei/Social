package com.appcoding.social.data.api

import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {

    @GET("comments/{postId}")
    suspend fun getComments(@Path("postId") postId : Long) : List<CommentResponse>

    @POST("comments/add")
    suspend fun addComment(@Body commentRequest : CommentRequest) : CommentResponse

}