package com.appcoding.social.data

import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService
{
    @Multipart
    @POST("posts/upload")
     fun addPost(@Part("neveshtak") neveshtak:RequestBody,
                        @Part image : MultipartBody.Part) : Call<String>

     @GET("posts/all")
     suspend fun getPost() : List<Post>

     @GET("comments/{postId}")
     suspend fun getComments(@Path("postId") postId : Long) : List<CommentResponse>

     @POST("comments/add")
     suspend fun addComment(@Body commentRequest : CommentRequest) : CommentResponse
}