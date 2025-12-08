package com.appcoding.social.screen.addpost

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.ActivityNavigatorExtras


@Composable
fun AddStory(){
    CameraPermissionWrapper {
        CameraPreview()
    }
}
