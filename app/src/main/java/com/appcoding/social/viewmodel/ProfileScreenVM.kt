package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.RetrofitInstance
import com.appcoding.social.UserPreferences
import com.appcoding.social.models.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
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
    val myUserid : StateFlow<Long> = _myUserid

    private val _myProfile = MutableStateFlow(false)
    val myProfile : StateFlow<Boolean> = _myProfile

    fun getProfile(userid : Long){
        viewModelScope.launch {
            _myUserid.value = userPreferences.getUserIdFlow().first() ?: 0L
           // _myProfile.value = myUserid.value == userid

           _isLoading.value = true
            try{
                _userInfo.value = RetrofitInstance.api.getUserInfo(_myUserid.value)
            }
            catch (ex : Exception){
                _message.value = ex.toString()
            }
            finally {
                _isLoading.value = false
            }
        }
    }


}