package com.appcoding.social.models

data class PostResponse(
    val id : Long,
    val caption : String,
    val date : String,
    val time : String,
    val userId : Long,
    val userProfile : String,
    val username : String,
    val likeCount : Long,
    val commentCount : Long,
    val image : String,
    val isLike : Boolean,
    val isSave : Boolean


)
