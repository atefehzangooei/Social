package com.appcoding.social.data.api

//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitInstance {

    //private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val BASE_URL = "http://192.168.100.4:8080/"
   //private const val BASE_URL = "http://10.80.158.110:8080/"

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