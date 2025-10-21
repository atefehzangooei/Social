package com.appcoding.social.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.api.RetrofitInstance
import com.appcoding.social.UserPreferences
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.UserInfo
import com.appcoding.social.models.pageSizeProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVM @Inject constructor(
    private val userPreferences: UserPreferences) : ViewModel()
{
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo : StateFlow<UserInfo?> = _userInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message : StateFlow<String> = _message

    private val _myUserid = MutableStateFlow(0L)

    private val _myProfile = MutableStateFlow(false)
    val myProfile : StateFlow<Boolean> = _myProfile

    private val _userPosts = MutableStateFlow<List<PostResponse>>(emptyList())
    val userPosts : StateFlow<List<PostResponse>> = _userPosts

    private val _postLoading = MutableStateFlow(false)
    val postLoading : StateFlow<Boolean> = _postLoading

    private val _success = MutableStateFlow(false)
    val success : StateFlow<Boolean> = _success

    private val _postSuccess = MutableStateFlow(false)
    val postSuccess : StateFlow<Boolean> = _postSuccess

    private val _lastSeenId = MutableStateFlow<Long?>(-1L)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> = _isRefreshing


    fun onRefresh(userid : Long){
        _lastSeenId.value = -1
        _isRefreshing.value = true
        getProfile(userid)
    }

    fun getProfile(userid : Long){
        viewModelScope.launch {
            _myUserid.value = userPreferences.getUserIdFlow().first() ?: 0L
            _myProfile.value = _myUserid.value == userid

            getUserPosts(userid)

           _isLoading.value = true
            _success.value = false
            try{
                _userInfo.value = RetrofitInstance.api.getUserInfo(_myUserid.value)
                _success.value = true
            }
            catch (ex : Exception){
                _message.value = ex.toString()
            }
            finally {
                _isLoading.value = false
            }
        }
    }


    fun getUserPosts(userid : Long){
        viewModelScope.launch {
            _postLoading.value = true
            _postSuccess.value = false

            try{
                val response = RetrofitInstance.api.getPostsByUserid(
                    userId = userid,
                    lastSeenId = _lastSeenId.value,
                    size = pageSizeProfile)
                Log.d("myresponse", "size is ${response.size}")

                if(_lastSeenId.value !!> -1)
                    _userPosts.value += response
                else
                    _userPosts.value = response

                _lastSeenId.value = response.lastOrNull()?.id
                _postSuccess.value = true
            }
            catch (ex : Exception){
                _message.value = ex.toString()
            }
            finally {
                _postLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

}