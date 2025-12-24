package com.appcoding.social.screen.components

import android.net.Uri
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
import com.appcoding.social.screen.addpost.AddImageToStory
import com.appcoding.social.screen.addpost.AddStory
import com.appcoding.social.screen.home.MainData
import com.appcoding.social.screen.home.StoryPager
import com.appcoding.social.screen.home.StoryViewer
import com.appcoding.social.screen.profile.ProfileScreen
import com.appcoding.social.splash.Splash


@Composable
fun AppNavHost(navController: NavHostController, startDestination : String = "splash")
{
    NavHost(navController = navController, startDestination = startDestination, route = "nav_graph"){
        composable("splash"){ Splash(navController) }
        composable("signin"){ SignIn(navController) }
        composable("signup"){ SignUp(navController) }
       // composable("main"){ MyApp(navController) }
        composable("forgetpassword"){ ForgetPassword(navController) }
        //composable("post_screen"){ MainData(navController) }

        composable(
            route = "story/{userid}",
            arguments = listOf(navArgument("userid") { type = NavType.LongType })
        ) { backStackEntry ->
            val userid = backStackEntry.arguments!!.getLong("userid")
            StoryPager(userid, navController)
        }

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
            ProfileScreen(userid, navController)
        }

        composable("start_from_index/{userid}/{username}/{index}/{tag}",
            arguments = listOf(
                navArgument("index"){type = NavType.IntType},
                navArgument("userid"){type = NavType.LongType},
                navArgument("username"){type = NavType.StringType},
                navArgument("tag"){type = NavType.StringType}
            )
        ){backStackEntry ->
            val index = backStackEntry.arguments!!.getInt("index")
            val userid = backStackEntry.arguments!!.getLong("userid")
            val username = backStackEntry.arguments!!.getString("username")
            val tag = backStackEntry.arguments!!.getString("tag")
            StartFromIndex(userid, username!!, index, navController, tag!!)
        }
        composable("addstory") { AddStory(navController) }

        composable("addimagestory") { AddImageToStory(navController) }
    }
}