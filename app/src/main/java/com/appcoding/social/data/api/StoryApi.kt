package com.appcoding.social.data.api

import com.appcoding.social.models.StoryRequest
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.StringMessage
import com.appcoding.social.models.UserStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoryApi {

    @GET("story/followers/{userId}")
    suspend fun getStoryOfFollowers(@Path("userId") userId : Long) : List<StoryResponse>

    @Multipart
    @POST("story/upload")
    suspend fun uploadStory(@Part image: MultipartBody.Part,
                         @Part("story") story: RequestBody) : StoryResponse

    @DELETE("story/delete/{storyId}")
    suspend fun deleteStory(@Path("storyId") storyId : Long) : StringMessage

    @GET("story/user/{userId}")
    suspend fun getStoryByUserid(@Path("userId") userId : Long) : List<UserStory>

}