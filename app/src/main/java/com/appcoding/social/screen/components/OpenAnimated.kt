package com.appcoding.social.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.appcoding.social.AddPostScreen

@Composable
fun OpenAnimated(
    isAddScreenVisible :Boolean,
    opening : @Composable () -> Unit

) {
    AnimatedVisibility(
        visible = isAddScreenVisible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    ) {
        opening()
    }
}
