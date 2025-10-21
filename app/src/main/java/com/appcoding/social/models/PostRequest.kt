package com.appcoding.social.models

data class PostRequest(
    val userId : Long,
    val neveshtak : String,
    val date : String,
    val time : String
)