package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel()
{
    private val _phone = MutableStateFlow("")
    val phone :StateFlow<String> = _phone

    private val _password = MutableStateFlow("")
    val password : StateFlow<String> = _password

    private val _usernanme = MutableStateFlow("")
    val username : StateFlow<String> = _usernanme

    private val _message = MutableStateFlow("")
    val message : StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success : StateFlow<Boolean> = _success

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading


    //Functions to update Inputs
    fun onPhoneChange(value : String){ _phone.value = value }
    fun onPasswordChange(value : String){ _password.value = value }
    fun onUsernameChange(value : String){ _usernanme.value = value }


    fun signup(){
        viewModelScope.launch {
            if (_phone.value.isBlank() || _usernanme.value.isBlank() || _password.value.isBlank()) {
                _message.value ="لطفا اطلاعات را به درستی وارد نمایید"
                return@launch
            }

        }
    }
}