package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.LikeRemoteRepository
import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.StringMessage

class LikeRepository(private val remoteRepository: LikeRemoteRepository) {

    suspend fun likePost(likeRequest : LikeRequest) : StringMessage {
        return remoteRepository.likePost(likeRequest)
    }

    suspend fun disLikePost(postId: Long,
                            userId : Long){
        return remoteRepository.disLikePost(postId, userId)
    }

}