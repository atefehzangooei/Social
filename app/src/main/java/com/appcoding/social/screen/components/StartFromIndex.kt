package com.appcoding.social.screen.components

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.home.PostCard
import com.appcoding.social.viewmodel.MainDataVM
import com.appcoding.social.viewmodel.ProfileScreenVM

@Composable
fun StartFromIndex(index : Int,
                   navController: NavHostController){

    val listState = rememberLazyListState()

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("nav_graph")
    }

    val viewModelMain : MainDataVM = hiltViewModel(parentEntry)
    val viewModel : ProfileScreenVM = hiltViewModel(parentEntry)
    val posts by viewModel.userPosts.collectAsState()

    LaunchedEffect(posts, index){
        listState.scrollToItem(index)
    }
    LazyColumn(modifier = Modifier
        .fillMaxSize(),
        state = listState) {
        items(posts){post ->
            PostCard(post = post,
                navController = navController,
                viewModel = viewModelMain)
        }
    }
}