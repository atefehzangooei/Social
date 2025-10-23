package com.appcoding.social.data.remote

import com.appcoding.social.data.api.SavePostApi
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StringMessage

class SavePostRemoteRepository(private val api: SavePostApi) {

    suspend fun savePost(savePostRequest : SavePostRequest) : StringMessage{
        return api.savePost(savePostRequest)
    }

    suspend fun unSavePost(postId: Long, userId : Long){
        return api.unSavePost(postId, userId)
    }
}
