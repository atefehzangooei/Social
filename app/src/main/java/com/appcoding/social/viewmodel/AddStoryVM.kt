package com.appcoding.social.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appcoding.social.data.repository.StoryRepository
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.screen.components.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStoryVM @Inject constructor(
    private val userPreferences: UserPreferences,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri : StateFlow<Uri?> = _selectedImageUri

    private val _profileImage = MutableStateFlow("")
    val profileImage : StateFlow<String> = _profileImage

    private val _storyState = MutableStateFlow(UiState())
    val storyState : StateFlow<UiState> = _storyState

    private val _uploadedStory = MutableStateFlow(null)
    val uploadedStory : StateFlow<StoryResponse?> = _uploadedStory


    fun shareStory(){
        viewModelScope.launch {
            _storyState.value = UiState(isUploading = true)

            _uploadedStory.value = storyRepository.addStory()

        }
    }

     fun onSelectedImage(imageUri : Uri){
         _selectedImageUri.value = imageUri
         getProfileImage()
    }

    private fun getProfileImage(){
        viewModelScope.launch {
            _profileImage.value = userPreferences.getUserProfileFlow().first() ?: ""
        }
    }

}