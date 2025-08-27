package com.appcoding.social

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun AppNavHost(navController: NavHostController, startDestination : String = "splash")
{
    NavHost(navController = navController, startDestination = startDestination){
        composable("splash"){ Splash(navController) }
        composable("signin"){ SignIn(navController) }
        composable("signup"){ SignUp(navController) }
        composable("main"){ MyApp(navController) }
        composable("forgetpassword"){ ForgetPassword(navController) }
    }
}