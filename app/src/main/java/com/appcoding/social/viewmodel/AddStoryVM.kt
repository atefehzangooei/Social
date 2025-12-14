package com.appcoding.social.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AddStoryVM : ViewModel() {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri : StateFlow<Uri?> = _selectedImageUri

     fun onSelectedImage(imageUri : Uri){
         _selectedImageUri.value = imageUri
    }
}