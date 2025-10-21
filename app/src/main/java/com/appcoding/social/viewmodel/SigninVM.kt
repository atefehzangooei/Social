package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.api.RetrofitInstance
import com.appcoding.social.UserPreferences
import com.appcoding.social.models.SigninRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninVM @Inject constructor(
    private val userPreferences: UserPreferences) : ViewModel()
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

    private val _userid = MutableStateFlow(-1L)
    val userid : StateFlow<Long> = _userid

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
                    _userid.value =  userInfo!!.id
                    userPreferences.saveUserId(_userid.value)

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