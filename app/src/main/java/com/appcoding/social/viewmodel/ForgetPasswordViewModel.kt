package com.appcoding.social.viewmodel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.RetrofitInstance
import com.appcoding.social.models.ForgetRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class ForgetPasswordViewModel : ViewModel() {

    private val _phone = MutableStateFlow("")
    val phone : StateFlow<String> = _phone

    private val _username = MutableStateFlow("")
    val username : StateFlow<String> = _username

    private val _message = MutableStateFlow("")
    val message : StateFlow<String> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _success = MutableStateFlow(false)
    val success : StateFlow<Boolean> = _success


    fun onPhoneChanged(value: String) { _phone.value = value }
    fun onUsernameChanged(value: String) { _username.value = value }

    fun forgetPassword(){
        viewModelScope.launch {
            if (_username.value.isBlank() || _phone.value.isBlank()) {
                _message.value = "لطفا تمام اطلاعات را وارد کنید"
                return@launch
            }
            _isLoading.value = true
                    try {
                        val response =
                            RetrofitInstance.api.forgetPassword(
                                ForgetRequest(
                                    _username.value,
                                    _phone.value
                                )
                            )
                        when(response.message) {
                            "no user" -> {
                                _message.value = "کاربری با این نام کاربری و شماره تلفن همراه وجود ندارد"
                            }
                            "sms" -> {
                                _message.value = "اطلاعات کاربری شما تا دقایقی دیگر برایتان ارسال می شود"
                                _success.value = true
                            }
                        }
                    }
                    catch(e: Exception){
                        _message.value = e.toString()
                    }
                    finally {
                        _isLoading.value = false
                    }

        }
    }

}