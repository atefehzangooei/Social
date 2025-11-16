package com.appcoding.social.screen.components

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class MyFileUtils @Inject constructor(
    @ApplicationContext private val context: Context) {

    fun uriToFile(uri: Uri): File {

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Uri is not valid")
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return tempFile
    }
}
