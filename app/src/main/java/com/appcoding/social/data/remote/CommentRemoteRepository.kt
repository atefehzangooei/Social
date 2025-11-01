package com.appcoding.social.data.remote

import com.appcoding.social.data.api.CommentApi
import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.StringMessage
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject

class CommentRemoteRepository @Inject constructor(private val api : CommentApi) {

    suspend fun getComments(postId : Long) : List<CommentResponse>{
        return api.getComments(postId)
    }

    suspend fun addComment(commentRequest : CommentRequest) : CommentResponse{
        return api.addComment(commentRequest)
    }

    suspend fun deleteComment(commentId : Long) : StringMessage{
        return api.deleteComment(commentId)
    }

}