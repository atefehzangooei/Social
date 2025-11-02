package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.repository.UserRepository
import com.appcoding.social.models.SignupRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignupVM @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel()
{
    private val _phone = MutableStateFlow("")
    val phone :StateFlow<String> = _phone

    private val _password = MutableStateFlow("")
    val password : StateFlow<String> = _password

    private val _usernanme = MutableStateFlow("")
    val username : StateFlow<String> = _usernanme

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state


    //Functions to update Inputs
    fun onPhoneChange(value : String){ _phone.value = value }
    fun onPasswordChange(value : String){ _password.value = value }
    fun onUsernameChange(value : String){ _usernanme.value = value }


    fun signup(){
        viewModelScope.launch {
            if (_phone.value.isBlank() || _usernanme.value.isBlank() || _password.value.isBlank()) {
                _state.value = UiState(message = "لطفا اطلاعات را به درستی وارد نمایید")
                return@launch
            }
            _state.value = UiState(isLoading = true)
            try {
                val response = userRepository.signUp(
                    SignupRequest(
                        phone = _phone.value,
                        username = _usernanme.value,
                        password = _password.value
                    )
                )
                when (response.message) {
                    "repeated username" -> {
                        _state.value = UiState(message = "نام کاربری وارد شده تکراری است")
                    }
                    "success" -> {
                        _state.value = UiState(success = true, message = "حساب شما با موفقیت ایجاد شد")
                    }
                    else -> {
                        _state.value = UiState(message = "نام کاربری دیگری انتخاب کنید")
                    }
                }
            } catch (e: Exception) {
                _state.value = UiState(message = e.toString())
            }
        }
    }
}