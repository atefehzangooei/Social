package com.appcoding.social.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddPostVM : ViewModel() {

    private val _neveshtak = MutableStateFlow("")
    val neveshtak : StateFlow<String> = _neveshtak

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state


    fun onNeveshtakChanged(value : String) { _neveshtak.value = value }

    fun sharePost(){

    }

}