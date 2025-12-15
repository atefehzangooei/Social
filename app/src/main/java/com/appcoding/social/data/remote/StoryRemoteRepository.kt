package com.appcoding.social.data.remote

import com.appcoding.social.data.api.StoryApi
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.StringMessage
import com.appcoding.social.models.UserStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRemoteRepository @Inject constructor(private val api : StoryApi) {

    suspend fun getStoryOfFollowers(userId : Long) : List<StoryResponse>{
        return api.getStoryOfFollowers(userId)
    }

    suspend fun uploadStory(image : MultipartBody.Part,
                         story : RequestBody) : StoryResponse {
        return api.uploadStory(image, story)
    }

    suspend fun deleteStory(storyId : Long) : StringMessage {
        return api.deleteStory(storyId)
    }

    suspend fun getStoryByUserid(userId : Long) : List<UserStory> {
        return api.getStoryByUserid(userId)
    }

}