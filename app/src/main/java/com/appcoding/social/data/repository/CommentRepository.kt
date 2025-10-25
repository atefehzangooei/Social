package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.CommentRemoteRepository
import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import javax.inject.Inject

class CommentRepository @Inject constructor(private val remoteRepository: CommentRemoteRepository) {

    suspend fun getComments(postId : Long) : List<CommentResponse>{
        return remoteRepository.getComments(postId)
    }

    suspend fun addComment(commentRequest : CommentRequest) : CommentResponse {
        return remoteRepository.addComment(commentRequest)
    }

}