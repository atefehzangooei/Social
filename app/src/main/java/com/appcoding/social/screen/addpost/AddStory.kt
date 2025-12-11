package com.appcoding.social.screen.addpost

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.ActivityNavigatorExtras
import com.appcoding.social.screen.components.checkReadPermission


@Composable
fun AddStory(){
    CameraPermissionWrapper {
        CameraPreview(onSwipUp = { DisplayGalleyImages() })
    }
}


@Composable
fun DisplayGalleyImages(){

    val context = LocalContext.current
    var images by remember { mutableStateOf(emptyList<Uri>()) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            images = getGalleryImages(context)
        }
    }

    val permission = checkReadPermission()

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            images = getGalleryImages(context)
        } else {
            permissionLauncher.launch(permission)
        }
    }

    Toast.makeText(context, "image size is ${images.size}",Toast.LENGTH_LONG).show()


}