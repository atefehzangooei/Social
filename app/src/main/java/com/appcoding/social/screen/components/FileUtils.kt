package com.appcoding.social.screen.components

import android.content.Context
import android.net.Uri
import java.io.File


fun uriToFile(uri: Uri, context: Context): File {

    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Uri is not valid")
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    return tempFile
}

