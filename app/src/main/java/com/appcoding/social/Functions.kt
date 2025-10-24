package com.appcoding.social

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.appcoding.social.ui.theme.Colors
import java.io.File

object Functions {

    @Composable
    fun RightToLeftLayout(
        content: @Composable () -> Unit
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection
                    provides LayoutDirection.Rtl
        ) {
            content()
        }
    }



    @Composable
    fun screenWidth(): Dp {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        return screenWidth
    }


    @Composable
    fun screenHeight(): Dp {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        return screenHeight
    }


    fun uriToFile(uri: Uri, context: Context): File {

            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Uri is not valid")
            val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

        return tempFile
    }




}