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

        // جلوگیری از آپلود دوباره وسط کار
        if (_state.value.isUploading) return
        viewModelScope.launch {
            val post = PostRequest(
                userId = userPreferences.getUserIdFlow().first() ?: 0L,
                caption = neveshtak,
                date = "",
                time = ""
                )

            try {
                _state.value = UiState(isUploading = true)

                // PostRequest → JSON
                val json = Gson().toJson(post)
                val postPart = json.toRequestBody("application/json".toMediaType())

                // Image با Progress
                val requestBody = UploadProgressRequestBody(
                    file = imageFile,
                    contentType = "image/jpeg".toMediaTypeOrNull(),
                    onProgress = { percent ->
                        _state.value = _state.value.copy(progress = percent)
                    }
                )

                // image file → Multipart
                val imagePart = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = imageFile.name,
                    body = requestBody
                   // body = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                // API call
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


    /*  private fun uploadPost(neveshtak : String, imageFile : File){
          viewModelScope.launch {
              try{
                  _state.value = UiState(isUploading = true, progress = 0)
                  val requestBody = UploadProgressRequestBody(
                      file = imageFile,
                      contentType = "image/jpeg",
                      onProgress = {


                      }
                  )

              }
              catch(ex : Exception){
                  _state.value = UiState(progress = 0, message = ex.toString())
              }
          }
      }*/

    /*
 fun uploadPost1(neveshtak : String, imageFile : File,
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
*/

}