package com.appcoding.social.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.data.repository.UserRepository
import com.appcoding.social.screen.components.UserPreferences
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.UserInfo
import com.appcoding.social.screen.components.pageSizeProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVM @Inject constructor(
    private val userPreferences: UserPreferences,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel()
{
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo : StateFlow<UserInfo?> = _userInfo

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state

    private val _postState = MutableStateFlow(UiState())
    val postState : StateFlow<UiState> = _postState

    private val _myUserid = MutableStateFlow(0L)

    private val _myProfile = MutableStateFlow(false)
    val myProfile : StateFlow<Boolean> = _myProfile

    private val _userPosts = MutableStateFlow<List<PostResponse>>(emptyList())
    val userPosts : StateFlow<List<PostResponse>> = _userPosts

    private val _lastSeenId = MutableStateFlow<Long?>(-1L)

    fun onRefresh(userid : Long){
        _lastSeenId.value = -1
        _state.value = UiState(isRefreshing = true)
        getProfile(userid)
    }

    fun getProfile(userid : Long){
        viewModelScope.launch {
            _myUserid.value = userPreferences.getUserIdFlow().first() ?: 0L
            _myProfile.value = _myUserid.value == userid

            getUserPosts(userid)

            try{
                _userInfo.value = userRepository.getUserInfo(_myUserid.value)
                userPreferences.updateProfileImage(_userInfo.value!!.profileImage)
                _state.value = UiState(success = true)
            }
            catch (ex : Exception){
                _state.value = UiState(message = ex.toString())
            }
        }
    }


    fun getUserPosts(userid : Long){
        viewModelScope.launch {
           // _postState.value = UiState(isLoading = true)
            try{
                val response = postRepository.getPostsByUserid(
                    userId = userid,
                    lastSeenId = _lastSeenId.value,
                    size = pageSizeProfile)
                Log.d("myresponse", "size is ${response.size}")

                if(response.size > 0) {
                    if (_lastSeenId.value!! > -1)
                        _userPosts.value += response
                    else
                        _userPosts.value = response

                    _lastSeenId.value = response.lastOrNull()?.id
                    _postState.value = UiState(success = true)
                }

            }
            catch (ex : Exception){
                _postState.value = UiState(message = ex.toString())
            }
        }
    }

}