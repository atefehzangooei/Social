package com.appcoding.social

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appcoding.social.screen.addpost.AddPostScreen
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.OpenAnimated
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.screen.explore.SearchPost
import com.appcoding.social.screen.home.HomeScreen
import com.appcoding.social.screen.profile.ProfileScreen
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MyApp(userid : Long, navController : NavHostController) {

    var selectedIndex by remember { mutableIntStateOf(0) }
    var isAddScreenVisible by remember { mutableStateOf(false) }

    val navigationItems = listOf(
        NavigationItem("Home",
            painterResource(R.drawable.home),
            painterResource(R.drawable.home_selected)
        ),
        NavigationItem("Search",
            painterResource(R.drawable.search),
            painterResource(R.drawable.search_selected)
        ),
        NavigationItem("Add",
            painterResource(R.drawable.add),
            painterResource(R.drawable.add)
        ),
        NavigationItem("Reels",
            painterResource(R.drawable.reel),
            painterResource(R.drawable.reel_selected)
        ),
        NavigationItem("Profile",
            painterResource(R.drawable.profile),
            painterResource(R.drawable.profile_selected)
        )

    )

    RightToLeftLayout{

        Scaffold(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),

            bottomBar = {
                if (!isAddScreenVisible) {
                    BottomNavigationBar(navigationItems = navigationItems,
                        selectedIndex = selectedIndex,
                        onItemSelected = {index ->
                            if (index == 2) {
                                isAddScreenVisible = true
                            } else {
                                selectedIndex = index
                            }
                        }
                    )
                }
            }
        )
        {contentPadding ->

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(color = Colors.background),
                contentAlignment = Alignment.Center)
            {
                if(userid <= 0L){
                    LoadingDataProgress()
                }
                else {
                    when (selectedIndex) {
                        0 -> HomeScreen(userid, navController)
                        1 -> SearchPost()
                        3 -> SearchPost()
                        4 -> ProfileScreen(userid = userid,
                            navController = navController)
                    }
                }
                OpenAnimated(isAddScreenVisible) {
                    AddPostScreen(onBack = { isAddScreenVisible = false })
                }

            }
        }
    }

}

@Composable
fun BottomNavigationBar(
    navigationItems : List<NavigationItem>,
    selectedIndex :Int,
    onItemSelected : (Int) -> Unit
){
    BottomAppBar(
        containerColor = Colors.bottom_appbar_container,
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.White)
            .shadow(elevation = Dimens.bottom_appbar_shadow)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            navigationItems.forEachIndexed { index, item ->
                IconButton(onClick = { onItemSelected(index) })
                {
                    Icon(
                        painter =
                        if (selectedIndex == index)
                            item.selected
                        else item.unselected,
                        contentDescription = "icon",
                        modifier = Modifier.size(Dimens.bottom_appbar_size)
                    )
                }
            }
        }
    }
}


data class NavigationItem(
    val label : String,
    val unselected : Painter,
    val selected : Painter
)

