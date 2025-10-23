package com.appcoding.social.data.remote

import com.appcoding.social.data.api.PostApi
import com.appcoding.social.models.PostResponse
import retrofit2.http.Path

class PostRemoteRepository(private val api : PostApi) {

    suspend fun getPostsByFollowers(userId : Long,
                                   lastSeenId : Long? = -1,
                                   size : Int) : List<PostResponse> {
        return api.getPostsByFollowers(userId, lastSeenId, size)
    }

    suspend fun getPostsByUserid(userId : Long,
                                 lastSeenId: Long?,
                                 size : Int) : List<PostResponse> {
        return api.getPostsByUserid(userId, lastSeenId, size)
    }

    suspend fun searchPost(text : String,
                           userId: Long,
                           lastSeenId: Long?,
                           size : Int) : List<PostResponse>{
        return api.searchPost(text, userId, lastSeenId, size)
    }
}