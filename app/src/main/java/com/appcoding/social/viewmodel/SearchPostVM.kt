package com.appcoding.social.viewmodel

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.components.UserPreferences
import com.appcoding.social.screen.components.pageSizeExplore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPostVM @Inject constructor(
    private val postRepository: PostRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText : StateFlow<String> = _searchText

    private val _isFocused = MutableStateFlow(false)
    val isFocused : StateFlow<Boolean> = _isFocused

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state

    private val _isTyping = MutableStateFlow(false)
    val isTyping : StateFlow<Boolean> = _isTyping

    private val _searchAction = MutableStateFlow(false)
    val searchAction : StateFlow<Boolean> = _searchAction

    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts : StateFlow<List<PostResponse>> = _posts

    private val _lastSeenId = MutableStateFlow<Long?>(-1L)

    fun onSearchTextChanged(newText : String){
        _searchText.value = newText
        _isTyping.value = true
    }

    fun onFocusChanged(state : FocusState){
        _isFocused.value = state.isFocused
    }

    fun closeSearch(){
        _isFocused.value = false
        _searchText.value = ""
        _isTyping.value = false
    }

    fun keyboardSearchAction(){
        _searchAction.value = true
    }

    fun getPosts(){
        viewModelScope.launch {
            val myUserid = userPreferences.getUserIdFlow().first() ?: 0L
            _state.value = UiState(success = false)
            _state.value = UiState(isLoading = true)
            try {
                val response = postRepository.searchPost(
                    text = _searchText.value,
                    userId = myUserid,
                    lastSeenId = _lastSeenId.value,
                    size = pageSizeExplore
                )
                if(_lastSeenId.value !!> -1)
                    _posts.value += response
                else
                    _posts.value = response

                _lastSeenId.value = response.lastOrNull()?.id
                _state.value = UiState(success = true)

            } catch (e: Exception) {
                _state.value = UiState(message = e.toString())
                _state.value = UiState(success = false)
            } finally {
                _searchAction.value = false
                _state.value = UiState(isLoading = false)
            }
        }
    }
}


