package com.appcoding.social.data.remote

import com.appcoding.social.data.api.PostApi
import com.appcoding.social.models.PostRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.StringMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Path
import javax.inject.Inject

class PostRemoteRepository @Inject constructor(private val api : PostApi) {

    suspend fun uploadPost(image : MultipartBody.Part,
                           post : RequestBody
    ) : PostResponse {
        return api.uploadPost(image, post)
    }


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