package com.appcoding.social.models

data class SavePostRequest(
    val userId : Long,
    val postId : Long,
    val date : String,
    val time : String
)