package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.screen.components.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
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
