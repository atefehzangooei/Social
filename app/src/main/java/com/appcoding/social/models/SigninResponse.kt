package com.appcoding.social.models

data class SigninResponse (
    val id : Long,
    val username : String,
    val password : String,
    val phone : String,
    val email : String,
    val link : String,
    val bio :String,
    val profileImage : String,
    val date : String,
    val time :String
)