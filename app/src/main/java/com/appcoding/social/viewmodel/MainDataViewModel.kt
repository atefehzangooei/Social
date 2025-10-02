package com.appcoding.social.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appcoding.social.RetrofitInstance
import com.appcoding.social.UserPreferences
import com.appcoding.social.models.PostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDataViewModel @Inject constructor(
    private val userPreferences: UserPreferences

) : ViewModel() {

    private val pageSize = 10

    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts : StateFlow<List<PostResponse>> = _posts

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> = _isRefreshing

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _lastSeenId = MutableStateFlow<Long?>(-1L)
    val lastSeenId : StateFlow<Long?> = _lastSeenId

    private val _message = MutableStateFlow("")
    val message : StateFlow<String> = _message

    private val _userid = MutableStateFlow(-1L)
    val userid :StateFlow<Long> = _userid

    fun getData(){
        viewModelScope.launch {
            try{
                userPreferences.getUserIdFlow().collect{ id ->
                    _userid.value = id ?: 0L}

                _isLoading.value = true
                val response = RetrofitInstance.api.getPostsByFollower(
                    userId = _userid.value,
                    lastSeenId = _lastSeenId.value,
                    size =  pageSize)
                _posts.value = response
                _lastSeenId.value =response.lastOrNull()?.id
            }
            catch(e : Exception){
               _message.value = "خطایی رخ داده است"
            }
            finally {
                _isLoading.value = false
            }
        }
    }


}