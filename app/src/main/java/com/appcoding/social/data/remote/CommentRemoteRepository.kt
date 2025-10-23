package com.appcoding.social.data.remote

import com.appcoding.social.data.api.CommentApi
import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class CommentRemoteRepository(private val api : CommentApi) {

    suspend fun getComments(postId : Long) : List<CommentResponse>{
        return api.getComments(postId)
    }

    suspend fun addComment(commentRequest : CommentRequest) : CommentResponse{
        return api.addComment(commentRequest)
    }

}