package com.appcoding.social.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors

@Composable
fun HomeScreen(userid : Long, navController: NavHostController) {

    RightToLeftLayout {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Colors.background))
        {
            AppNameBar()
            MainData(navController)
        }
    }

}
