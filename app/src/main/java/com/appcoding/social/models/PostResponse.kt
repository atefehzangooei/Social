package com.appcoding.social.models

const val pageSizeHome = 10
const val pageSizeProfile = 12

data class PostResponse(
    val id : Long,
    val caption : String,
    val date : String,
    val time : String,
    val userId : Long,
    val userProfile : String,
    val username : String,
    val likeCount : Long,
    var commentCount : Long,
    val image : String,
    var isLike : Boolean,
    val isSave : Boolean


)
