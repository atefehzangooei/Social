package com.appcoding.social.screen.addpost

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionWrapper(
    onPermissionGranted : @Composable () -> Unit
){
    val cameraPermission = Manifest.permission.CAMERA

    val permissionState = rememberPermissionState(cameraPermission)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when{
        permissionState.status.isGranted ->{
            onPermissionGranted()
        }

        permissionState.status.shouldShowRationale -> {

        }
        else -> {

        }
    }
}