package com.appcoding.social

import com.appcoding.social.data.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

   /* var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
*/

  /*  val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // همه چی رو لاگ کن
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
*/
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
           // .client(client)
            .build()
            .create(ApiService::class.java)
    }
}