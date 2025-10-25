package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.api.RetrofitInstance
import com.appcoding.social.data.repository.UserRepository
import com.appcoding.social.models.ForgetRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordVM @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

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
                            userRepository.forgetPassword(
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