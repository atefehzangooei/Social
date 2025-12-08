package com.appcoding.social.screen.addpost

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.appcoding.social.Manifest


@Composable
fun AddStory(){
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

}
