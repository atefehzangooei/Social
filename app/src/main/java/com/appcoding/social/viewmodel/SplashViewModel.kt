package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appcoding.social.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SplashViewModel(
    private val userPreferences : UserPreferences
) : ViewModel() {

    private val _userid = MutableStateFlow<Long>(-1)
    val userid : StateFlow<Long> = _userid

    init {
        viewModelScope.launch {
            val id = userPreferences.getUserIdFlow().first() ?: 0L
            _userid.value = id
        }
    }
}
