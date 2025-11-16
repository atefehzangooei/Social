package com.appcoding.social.viewmodel

data class UiState(
    val isLoading : Boolean = false,
    val success : Boolean = false,
    val message : String = "",
    val isRefreshing : Boolean = false,
    val progress : Int = 0,
    val isUploading : Boolean = false
)