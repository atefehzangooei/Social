package com.appcoding.social.models



data class PostResponse(
    val id : Long,
    val caption : String,
    val date : String,
    val time : String,
    val userId : Long,
    val userProfile : String,
    val username : String,
    var likeCount : Int,
    var commentCount : Int,
    val image : String,
    var isLike : Boolean,
    var isSave : Boolean


)
