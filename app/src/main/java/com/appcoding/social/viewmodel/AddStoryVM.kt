package com.appcoding.social.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appcoding.social.data.repository.StoryRepository
import com.appcoding.social.models.StoryRequest
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.screen.addpost.UploadProgressRequestBody
import com.appcoding.social.screen.components.UploadPostProgress
import com.appcoding.social.screen.components.UserPreferences
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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

    private val _uploadedStory = MutableStateFlow<StoryResponse?>(null)
    val uploadedStory : StateFlow<StoryResponse?> = _uploadedStory


    fun uploadStory(imageFile : File){
        if(_storyState.value.isUploading) return
        viewModelScope.launch {
            Log.d("upload story", "is uploading")
            _storyState.value = UiState(isUploading = true)

            val userid = userPreferences.getUserIdFlow().first() ?: -1L

            val newStory = StoryRequest(
                userId = userid,
                date = "",
                time = ""
            )
            try{
                Log.d("upload story", "craete json")

                val json = Gson().toJson(newStory)
                val storyPart = json.toRequestBody(("application/json".toMediaType()))

                Log.d("upload story", "create request body")

                val requestBody = UploadProgressRequestBody(
                    file = imageFile,
                    contentType = "image/jpeg".toMediaType(),
                    onProgress = { percent ->
                        _storyState.value = _storyState.value.copy(progress = percent)
                    }
                )

                Log.d("upload story", "image part")

                val imagePart = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = imageFile.name,
                    body = requestBody
                )


                _uploadedStory.value = storyRepository.uploadStory(imagePart, storyPart)

                Log.d("upload story", "uploaded new story")

                _storyState.value = UiState(
                    isUploading = false,
                    progress = 100,
                    success = true,
                )
            }
            catch(e : Exception){
                Log.d("upload story", e.toString())

                _storyState.value = UiState(
                    isUploading = false,
                    success = false,
                    progress = 0,
                    message = e.toString()
                )
            }
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