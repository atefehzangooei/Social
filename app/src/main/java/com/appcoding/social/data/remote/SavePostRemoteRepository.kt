package com.appcoding.social.data.remote

import com.appcoding.social.data.api.SavePostApi
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StringMessage
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

class SavePostRemoteRepository(private val api: SavePostApi) {

    suspend fun savePost(savePostRequest : SavePostRequest) : StringMessage{
        return api.savePost(savePostRequest)
    }

    suspend fun unSavePost(postId: Long, userId : Long){
        return api.unSavePost(postId, userId)
    }
}
