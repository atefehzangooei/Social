package com.appcoding.social.data.repository

import com.appcoding.social.data.remote.PostRemoteRepository
import com.appcoding.social.models.PostRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.StringMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Path
import javax.inject.Inject

class PostRepository @Inject constructor(private val remoteRepository: PostRemoteRepository) {

    suspend fun uploadPost(image : MultipartBody.Part,
                           post : RequestBody
    ) : PostResponse {
        return remoteRepository.uploadPost(image, post)
    }


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

    suspend fun getAllPosts(userId: Long,
                            lastSeenId: Long?,
                            size: Int) : List<PostResponse> {
        return remoteRepository.getAllPosts(userId, lastSeenId, size)
    }
}