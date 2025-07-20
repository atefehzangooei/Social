package com.appcoding.social.models

data class User(
    val userid : Int,
    val name : String,
    val username : String,
    val email : String,
    val password : String,
    val phone : String,
    val profileImage : String,
    val bio : String,
    val link : String,
    val follower : Int,
    val following : Int,
    val postCount : Int
)