package com.appcoding.social.screen.components

import android.os.Build
import java.security.Permission

fun checkReadPermission() : String{

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

}