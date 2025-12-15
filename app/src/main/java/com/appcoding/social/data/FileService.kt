package com.appcoding.social.data

import androidx.compose.runtime.Composable
import com.appcoding.social.screen.addpost.UploadProgressRequestBody
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Composable
fun CreateFilePart(imageFilw : File){

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
}