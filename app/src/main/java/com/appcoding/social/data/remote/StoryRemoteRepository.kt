package com.appcoding.social.data.remote

import com.appcoding.social.data.api.StoryApi
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.StringMessage
import okhttp3.MultipartBody
import javax.inject.Inject

class StoryRemoteRepository @Inject constructor(private val api : StoryApi) {

    suspend fun getStoryOfFollowers(userId : Long) : List<StoryResponse>{
        return api.getStoryOfFollowers(userId)
    }

    suspend fun addStory(imageFile : MultipartBody.Part,
                         userId : Long,
                         date : String,
                         time : String){
        return api.addStory(imageFile, userId, date, time)
    }

    suspend fun deleteStory(storyId : Long) : StringMessage {
        return api.deleteStory(storyId)
    }

    suspend fun getStoryByUserid(userId : Long){
        return api.getStoryByUserid(userId)
    }

}