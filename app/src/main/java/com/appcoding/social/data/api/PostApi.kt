package com.appcoding.social.data.api

import com.appcoding.social.models.PostRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.StringMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PostApi {

    @Multipart
    @POST("posts/upload")
    suspend fun uploadPost(@Part image : MultipartBody.Part,
                @Part("post") post : RequestBody
    ) : PostResponse

    @GET("posts/follower/{userId}/{lastSeenId}/{size}")
    suspend fun getPostsByFollowers(@Path("userId") userId : Long,
                                    @Path("lastSeenId") lastSeenId : Long?,
                                    @Path("size") size : Int) : List<PostResponse>


    @GET("posts/all/{userId}/{lastSeenId}/{size}")
    suspend fun getPostsByUserid(@Path("userId") userId : Long,
                                 @Path("lastSeenId") lastSeenId: Long?,
                                 @Path("size") size : Int) : List<PostResponse>


    @GET("posts/search/{text}/{userId}/{lastSeenId}/{size}")
    suspend fun searchPost(@Path("text") text : String,
                           @Path("userId") userId: Long,
                           @Path("lastSeenId") lastSeenId: Long?,
                           @Path("size") size : Int) : List<PostResponse>


}