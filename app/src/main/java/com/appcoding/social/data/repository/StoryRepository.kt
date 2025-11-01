package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.StoryRemoteRepository
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.StringMessage
import okhttp3.MultipartBody
import javax.inject.Inject

class StoryRepository @Inject constructor(private val remoteRepository: StoryRemoteRepository) {

    suspend fun getStoryOfFollowers(userId : Long) : List<StoryResponse>{
        return remoteRepository.getStoryOfFollowers(userId)
    }

    suspend fun addStory(imageFile : MultipartBody.Part,
                         userId : Long,
                         date : String,
                         time : String){
        return remoteRepository.addStory(imageFile, userId, date, time)
    }

    suspend fun deleteStory(storyId : Long) : StringMessage {
        return remoteRepository.deleteStory(storyId)
    }

    suspend fun getStoryByUserid(userId : Long){
        return remoteRepository.getStoryByUserid(userId)
    }

}