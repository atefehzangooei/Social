package com.appcoding.social.data.api

//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitInstance {

    //private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val BASE_URL = "http://192.168.100.4:8080/"


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
           // .client(client)
            .build()
            //.create(ApiService::class.java)
    }

    val postApi : PostApi by lazy{
        retrofit.create(PostApi::class.java)
    }

    val commentApi : CommentApi by lazy {
        retrofit.create(CommentApi::class.java)
    }

    val userApi : UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val likeApi : LikeApi by lazy {
        retrofit.create(LikeApi::class.java)
    }

    val savePostApi : SavePostApi by lazy {
        retrofit.create(SavePostApi::class.java)
    }

    val storyApi : StoryApi by lazy {
        retrofit.create(StoryApi::class.java)
    }


}