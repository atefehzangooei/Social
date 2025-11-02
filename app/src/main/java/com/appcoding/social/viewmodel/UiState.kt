package com.appcoding.social.viewmodel

data class UiState(
    val isLoading : Boolean = false,
    val success : Boolean = false,
    val message : String = "",
    val isRefreshing : Boolean = false,
){
    fun setLoading(value : Boolean) = this.copy(isLoading = value, success = false, isRefreshing = false)
}
