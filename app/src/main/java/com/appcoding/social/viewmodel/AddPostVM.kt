package com.appcoding.social.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appcoding.social.data.api.PostApi
import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.models.PostRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.addpost.UploadProgressRequestBody
import com.appcoding.social.screen.components.MyFileUtils
import com.appcoding.social.screen.components.UserPreferences
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPostVM @Inject constructor(
    private val myFileUtils: MyFileUtils,
    private val postRepository: PostRepository,
    private val userPreferences: UserPreferences
): ViewModel() {

    private val _neveshtak = MutableStateFlow("")
    val neveshtak : StateFlow<String> = _neveshtak

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state

    private val _uploadedPost = MutableStateFlow<PostResponse?>(null)
    val uploadedPost : StateFlow<PostResponse?> = _uploadedPost

    private val _userid = MutableStateFlow(-1L)
    val userid : StateFlow<Long> = _userid


    fun onNeveshtakChanged(value : String) { _neveshtak.value = value }

    fun sharePost(neveshtak : String, selectedImageUri : Uri?){
        if(selectedImageUri == null){
            _state.value = UiState(message = "لطفا تصویر مورد نظر خود را انتخاب نمایید")
        }
        else {
            if (neveshtak.isEmpty()) {
                _state.value = UiState(message = "برای پست خود یک نوشتک زیبا بنویس")
            }
            else {
                    selectedImageUri.let { safeUri ->
                        val imageFile = myFileUtils.uriToFile(safeUri)
                        uploadPost(neveshtak, imageFile)
                    }
            }
        }
    }

    private fun uploadPost(neveshtak: String, imageFile: File){

        if (_state.value.isUploading) return
        viewModelScope.launch {
            _userid.value = userPreferences.getUserIdFlow().first() ?: -1L

            val post = PostRequest(
                userId = _userid.value,
                caption = neveshtak,
                date = "",
                time = ""
                )

            try {
                _state.value = UiState(isUploading = true)

                val json = Gson().toJson(post)
                val postPart = json.toRequestBody("application/json".toMediaType())

                val requestBody = UploadProgressRequestBody(
                    file = imageFile,
                    contentType = "image/jpeg".toMediaTypeOrNull(),
                    onProgress = { percent ->
                        _state.value = _state.value.copy(progress = percent)
                    }
                )

                val imagePart = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = imageFile.name,
                    body = requestBody
                )

                _uploadedPost.value = postRepository.uploadPost(imagePart, postPart)

                _state.value = UiState(
                    isUploading = false,
                    progress = 100,
                    success = true,
                )

            } catch (e: Exception) {
                _state.value = UiState(
                    isUploading = false,
                    success = false,
                    progress = 0,
                    message = e.toString()
                )
            }
        }
    }
}