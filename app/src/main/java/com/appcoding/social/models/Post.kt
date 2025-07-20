package com.appcoding.social.models

data class Post(
    val id : Long,
    val userId : Long,
    val userProfile : String,
    val username : String,
    val image : String,
    val likeCount : Long,
    val commentCount : Long,
    val caption : String,
    val date : String,
    val time : String

)
