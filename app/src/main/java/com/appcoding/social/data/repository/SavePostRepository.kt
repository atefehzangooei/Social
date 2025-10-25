package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.SavePostRemoteRepository
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StringMessage
import javax.inject.Inject

class SavePostRepository @Inject constructor(private val remoteRepository: SavePostRemoteRepository) {

    suspend fun savePost(savePostRequest : SavePostRequest) : StringMessage {
        return remoteRepository.savePost(savePostRequest)
    }

    suspend fun unSavePost(postId: Long, userId : Long){
        return remoteRepository.unSavePost(postId, userId)
    }
}