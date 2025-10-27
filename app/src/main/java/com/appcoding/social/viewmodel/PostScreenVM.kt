package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import com.appcoding.social.screen.components.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PostScreenVM @Inject constructor(
    private val userPreferences: UserPreferences
): ViewModel() {

    private val _commentSheetState = MutableStateFlow(false)
    val commentSheetState : StateFlow<Boolean> = _commentSheetState


}