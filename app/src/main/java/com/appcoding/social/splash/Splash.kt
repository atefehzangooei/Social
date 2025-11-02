package com.appcoding.social.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appcoding.social.screen.components.AppNavHost
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.SocialTheme
import com.appcoding.social.viewmodel.SplashVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SignUpIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialTheme {
                val navController = rememberNavController()
                AppNavHost(navController,"splash")
            }
        }
    }
}

@Composable
fun Splash(navController: NavHostController){

    val viewModel : SplashVM = hiltViewModel()
    val userid by viewModel.userid.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.appcolor)
    )

    LaunchedEffect(Unit) {
        delay(3000)
        if(userid > 0){
            navController.navigate("main/$userid"){
                popUpTo("splash"){inclusive = true}
            }
        }
        else {
            navController.navigate("signin") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
}