package com.appcoding.social.models

data class PostRequest(
    val userId : Long,
    val caption : String,
    val date : String,
    val time : String
)