package com.appcoding.social.data.remote

import com.appcoding.social.data.api.StoryApi
import com.appcoding.social.models.StoryResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

class StoryRemoteRepository(private val api : StoryApi) {

    suspend fun getStoryOfFollowers(userId : Long) : List<StoryResponse>{
        return api.getStoryOfFollowers(userId)
    }

    suspend fun addStory(imageFile : MultipartBody.Part,
                         userId : Long,
                         date : String,
                         time : String){
        return api.addStory(imageFile, userId, date, time)
    }

    suspend fun deleteStory(storyId : Long){
        return api.deleteStory(storyId)
    }

    suspend fun getStoryByUserid(userId : Long){
        return api.getStoryByUserid(userId)
    }

}