package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.PostRemoteRepository
import com.appcoding.social.models.PostResponse
import retrofit2.http.Path

class PostRepository(private val remoteRepository: PostRemoteRepository) {

    suspend fun getPostsByFollowers(userId : Long,
                                   lastSeenId : Long? = -1,
                                   size : Int) : List<PostResponse>{
        return remoteRepository.getPostsByFollowers(userId, lastSeenId, size)
    }

    suspend fun getPostsByUserid(userId : Long,
                                 lastSeenId: Long?,
                                 size : Int) : List<PostResponse> {
        return remoteRepository.getPostsByUserid(userId, lastSeenId, size)
    }

    suspend fun searchPost(text : String,
                           userId: Long,
                           lastSeenId: Long?,
                           size : Int) : List<PostResponse>{
        return remoteRepository.searchPost(text, userId, lastSeenId, size)
    }
}