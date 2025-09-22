package com.appcoding.social.viewmodel

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.RetrofitInstance
import com.appcoding.social.UserPreferences
import com.appcoding.social.models.SigninRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class SigninViewModel(
    private val userPreferences: UserPreferences,
    private val context : Context) : ViewModel()
{

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message : StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success : StateFlow<Boolean> = _success

    fun onUsernameChanged(value: String){ _username.value = value }
    fun onPasswordChanged(value: String){ _password.value = value }

    fun signin(){
        viewModelScope.launch {

            if (_username.value.isBlank() || _password.value.isBlank()) {
                _message.value = "لطفا تمام اطلاعات را وارد کنید"
                return@launch
            }
            _isLoading.value = true

            try {
                val response =
                    RetrofitInstance.api.signIn(
                        SigninRequest(
                            username = _username.value,
                            password = _password.value
                        )
                    )
                if (response.isSuccessful) {
                    _success.value = true
                    val userInfo = response.body()
                    userPreferences.saveUserId(context, userInfo!!.id)

                } else if (response.code() == 401) {
                    _message.value = "نام کاربری یا کلمه عبور اشتباه است!"
                }
            } catch (e: Exception) {
                _message.value = e.toString()
            } finally {
                _isLoading.value = false
            }

        }
    }
}