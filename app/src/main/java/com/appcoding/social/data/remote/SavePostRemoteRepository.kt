package com.appcoding.social.data.remote

import com.appcoding.social.data.api.SavePostApi
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StringMessage
import javax.inject.Inject

class SavePostRemoteRepository @Inject constructor(private val api: SavePostApi) {

    suspend fun savePost(savePostRequest : SavePostRequest) : StringMessage{
        return api.savePost(savePostRequest)
    }

    suspend fun unSavePost(postId: Long, userId : Long) : StringMessage
    {
        return api.unSavePost(postId, userId)
    }
}
