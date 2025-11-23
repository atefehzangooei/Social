package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.repository.UserRepository
import com.appcoding.social.screen.components.UserPreferences
import com.appcoding.social.models.SigninRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninVM @Inject constructor(
    private val userPreferences: UserPreferences,
    private val userRepository: UserRepository
) : ViewModel()
{

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state

    private val _userid = MutableStateFlow(-1L)
    val userid : StateFlow<Long> = _userid

    private val _profileImage = MutableStateFlow("")
    val profileImage : StateFlow<String> = _profileImage

    fun onUsernameChanged(value: String){ _username.value = value }
    fun onPasswordChanged(value: String){ _password.value = value }

    fun signin(){
        viewModelScope.launch {

            if (_username.value.isBlank() || _password.value.isBlank()) {
                _state.value = UiState(message = "لطفا تمام اطلاعات را وارد کنید")
                return@launch
            }
            _state.value = UiState(isLoading = true)

            try {
                val response =
                   userRepository.signIn(
                        SigninRequest(
                            username = _username.value,
                            password = _password.value
                        )
                    )
                if (response.isSuccessful) {
                    _state.value = UiState(success = true)
                    val userInfo = response.body()
                    _userid.value =  userInfo!!.id
                    _profileImage.value =  userInfo.profileImage
                    userPreferences.saveUser(_userid.value, _profileImage.value)

                } else if (response.code() == 401) {
                    _state.value = UiState(message = "نام کاربری یا کلمه عبور اشتباه است!")
                }
            } catch (e: Exception) {
                _state.value = UiState(message = e.toString())
            }
        }
    }
}