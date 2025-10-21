package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.api.RetrofitInstance
import com.appcoding.social.models.SignupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class SignupVM : ViewModel()
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
            _isLoading.value = true


            try {
                val response = RetrofitInstance.api.signUp(
                    SignupRequest(
                        phone = _phone.value,
                        username = _usernanme.value,
                        password = _password.value
                    )
                )
                when (response.message) {
                    "repeated username" -> {
                        _message.value = "نام کاربری وارد شده تکراری است"
                    }
                    "success" -> {
                        _success.value = true
                        _message.value = "حساب شما با موفقیت ایجاد شد"
                    }
                    else -> {
                        _message.value = "نام کاربری دیگری انتخاب کنید"
                    }
                }
            } catch (e: Exception) {
                _message.value = e.toString()
            } finally {
                _isLoading.value = false
            }

        }
    }
}