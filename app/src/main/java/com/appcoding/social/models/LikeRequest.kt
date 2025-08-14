package com.appcoding.social.models

data class LikeRequest (
    val postId : Long,
    val userId : Long,
    val date : String,
    val time : String
)