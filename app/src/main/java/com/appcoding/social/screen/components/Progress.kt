package com.appcoding.social.screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appcoding.social.ui.theme.Colors

@Composable
fun LoadingDataProgress(){
    CircularProgressIndicator(
        modifier = Modifier.size(30.dp),
        color = Colors.progress_color)
}