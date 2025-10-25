package com.appcoding.social.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.appcoding.social.MyApp
import com.appcoding.social.auth.ForgetPassword
import com.appcoding.social.auth.SignIn
import com.appcoding.social.auth.SignUp
import com.appcoding.social.screen.profile.ProfileScreen
import com.appcoding.social.splash.Splash


@Composable
fun AppNavHost(navController: NavHostController, startDestination : String = "splash")
{
    NavHost(navController = navController, startDestination = startDestination){
        composable("splash"){ Splash(navController) }
        composable("signin"){ SignIn(navController) }
        composable("signup"){ SignUp(navController) }
       // composable("main"){ MyApp(navController) }
        composable("forgetpassword"){ ForgetPassword(navController) }
        //composable("story"){ DisplayStory(navController) }

        composable(
            route = "main/{userid}",
            arguments = listOf(navArgument("userid") { type = NavType.LongType })
        ) { backStackEntry ->
            val userid = backStackEntry.arguments!!.getLong("userid")
            MyApp(userid, navController)
        }

        composable(
            route = "profile/{userid}",
            arguments = listOf(navArgument("userid") { type = NavType.LongType })
        ) { backStackEntry ->
            val userid = backStackEntry.arguments!!.getLong("userid")
            ProfileScreen(userid)
        }

        /*composable("displaysearch/{userid}",
            arguments = listOf(navArgument("userid"){type = NavType.LongType})
        ){backStackEntry ->
            val userid = backStackEntry.arguments!!.getLong("userid")
            DisplaySearchPosts()
        }*/
    }
}