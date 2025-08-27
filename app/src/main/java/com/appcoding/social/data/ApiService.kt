package com.appcoding.social.data

import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.ForgetRequest
import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.SavePostResponse
import com.appcoding.social.models.SigninRequest
import com.appcoding.social.models.SigninResponse
import com.appcoding.social.models.SignupRequest
import com.appcoding.social.models.StringMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService
{
    @Multipart
    @POST("posts/upload")
     fun addPost(@Part("neveshtak") neveshtak:RequestBody,
                        @Part image : MultipartBody.Part) : Call<String>

     @GET("posts/all/{userId}")
     suspend fun getPost(@Path("userId") userId : Long) : List<PostResponse>

     @GET("comments/{postId}")
     suspend fun getComments(@Path("postId") postId : Long) : List<CommentResponse>

     @POST("comments/add")
     suspend fun addComment(@Body commentRequest : CommentRequest) : CommentResponse

     @POST("likes")
     suspend fun likePost(@Body likeRequest : LikeRequest) : StringMessage

     @DELETE("likes/dislike/{postId}/{userId}")
     suspend fun disLikePost(@Path("postId") postId: Long,
                             @Path("userId") userId : Long)

     @POST("save_post")
     suspend fun savePost(@Body savePostRequest : SavePostRequest) : StringMessage

     @DELETE("save_post/{postId}/{userId}")
     suspend fun unSavePost(@Path("postId") postId: Long,
                             @Path("userId") userId : Long)

     @POST("users/signup")
     suspend fun signUp(@Body signupRequest: SignupRequest) : StringMessage

     @POST("users/signin")
     suspend fun signIn(@Body signinRequest: SigninRequest) : Response<SigninResponse>

     @POST("users/forgetpassword")
     suspend fun forgetPassword(@Body forgetRequest: ForgetRequest) : StringMessage

}