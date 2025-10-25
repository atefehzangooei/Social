package com.appcoding.social.data.api

import com.appcoding.social.screen.components.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun providePostApi(retrofit : Retrofit) : PostApi = retrofit.create(PostApi::class.java)

    @Provides
    @Singleton
    fun provideCommentApi(retrofit : Retrofit) : CommentApi = retrofit.create(CommentApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit : Retrofit) : UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideLikeApi(retrofit : Retrofit) : LikeApi = retrofit.create(LikeApi::class.java)

    @Provides
    @Singleton
    fun provideStoryApi(retrofit : Retrofit) : StoryApi = retrofit.create(StoryApi::class.java)

    @Provides
    @Singleton
    fun provideSavePostApi(retrofit : Retrofit) : SavePostApi = retrofit.create(SavePostApi::class.java)

}