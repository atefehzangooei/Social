package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.StoryRemoteRepository
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.StringMessage
import com.appcoding.social.models.UserStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(private val remoteRepository: StoryRemoteRepository) {

    suspend fun getStoryOfFollowers(userId : Long) : List<StoryResponse>{
        return remoteRepository.getStoryOfFollowers(userId)
    }

    suspend fun uploadStory(image : MultipartBody.Part,
                         story : RequestBody) : StoryResponse {
        return remoteRepository.uploadStory(image, story)
    }

    suspend fun deleteStory(storyId : Long) : StringMessage {
        return remoteRepository.deleteStory(storyId)
    }

    suspend fun getStoryByUserid(userId : Long) : List<UserStory> {
        return remoteRepository.getStoryByUserid(userId)
    }

}