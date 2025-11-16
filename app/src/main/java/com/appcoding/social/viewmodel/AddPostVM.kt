package com.appcoding.social.viewmodel

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import androidx.lifecycle.ViewModel
import com.appcoding.social.data.api.PostApi
import com.appcoding.social.data.repository.PostRepository
import com.appcoding.social.screen.components.MyFileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddPostVM @Inject constructor(
    private val myFileUtils: MyFileUtils,
    private val postRepository: PostRepository
): ViewModel() {

    private val _neveshtak = MutableStateFlow("")
    val neveshtak : StateFlow<String> = _neveshtak

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state


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
                _state.value = UiState(isLoading = true)
                    selectedImageUri.let { safeUri ->
                        val imageFile = myFileUtils.uriToFile(safeUri)
                        uploadPost(neveshtak, imageFile) { success, message ->
                            isUploading = false
                            uploadComplete = true
                            toastMessage = message
                        }
                    }
            }
        }
    }


 fun uploadPost(neveshtak : String, imageFile : File,
                       apiService : ApiService, callback : (Boolean, String) -> Unit){
    val textPart = neveshtak.toRequestBody("text/plain".toMediaTypeOrNull())
    val imagePart = MultipartBody.Part.createFormData(
        "image",
        imageFile.name,
        imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    )

    apiService.addPost(imagePart,textPart).enqueue(object : retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if(response.isSuccessful){
                callback(true, "upload successfully")
            }
            else {
                if(response.body()!=null) {
                        callback(false, response.body()!!)
                }
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            callback(false, "message is : ${t.message}")

        }
    })
}


}