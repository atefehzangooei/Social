package com.appcoding.social.viewmodel

import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.models.PostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchPostVM @Inject constructor(
    private val postRepository: PostRepository
) {
    private val _searchText = MutableStateFlow("")
    val searchText : StateFlow<String> = _searchText

    private val _isFocused = MutableSharedFlow(false)
    val isFocused : StateFlow<Boolean> = _isFocused

    private val _isTyping = MutableStateFlow(false)
    val isTyping : StateFlow<Boolean> = _isTyping

    private val _searchAction = MutableStateFlow(false)
    val searchAction : StateFlow<Boolean> = _searchAction

    private val _posts = MutableStateFlow<List<PostResponse>>()
}
