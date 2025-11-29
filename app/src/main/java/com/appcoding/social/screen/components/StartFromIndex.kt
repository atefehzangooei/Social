package com.appcoding.social.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.home.PostCard
import com.appcoding.social.screen.profile.ProfileScreen
import com.appcoding.social.viewmodel.MainDataVM
import com.appcoding.social.viewmodel.ProfileScreenVM

@Composable
fun StartFromIndex(userid : Long,
                   username : String,
                   index : Int,
                   navController: NavHostController){

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = index
    )

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("nav_graph")
    }

    val viewModelMain : MainDataVM = hiltViewModel(parentEntry)
    val viewModel : ProfileScreenVM = hiltViewModel(parentEntry)
    val posts by viewModel.userPosts.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.size(15.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
               contentDescription = "back to profile",
                tint = Color.Black,
                modifier = Modifier
                    .clickable { navController.navigate("profile/${userid}"){
                        popUpTo("profile/$userid"){ saveState = true }
                    }
                    })

           Text(text = username,
               style = MaterialTheme.typography.bodyMedium,
               fontWeight = FontWeight.Bold,
               color = Color.Black)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    navController = navController,
                    viewModel = viewModelMain
                )
            }
        }
    }
}