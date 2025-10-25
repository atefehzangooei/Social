package com.appcoding.social.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.PullToRefreshLazyList
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.viewmodel.ProfileScreenVM
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(userid : Long) {

    RightToLeftLayout {

        val viewModel : ProfileScreenVM = hiltViewModel()

        val userInfo by viewModel.userInfo.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val message by viewModel.message.collectAsState()
        val myProfile by viewModel.myProfile.collectAsState()
        val success by viewModel.success.collectAsState()
        val posts by viewModel.userPosts.collectAsState()
        val postLoading by viewModel.postLoading.collectAsState()
        val postSuccess by viewModel.postSuccess.collectAsState()
        val isRefreshing by viewModel.isRefreshing.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        val snackScope = rememberCoroutineScope()
        val gridState = rememberLazyGridState()

        LaunchedEffect(Unit) {
            viewModel.getProfile(userid)
        }

        LaunchedEffect(message) {
            if(message.isNotEmpty()){
                snackScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo }
                .collect { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    val totalItems = layoutInfo.totalItemsCount

                    if (lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItems - 1) {
                        viewModel.getUserPosts(userid)
                    }
                }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if ((isLoading && postLoading) || isRefreshing)
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    LoadingDataProgress()
                }

            if (success && postSuccess) {

                PullToRefreshLazyList(
                    posts = posts,
                    profileList = listOf(userInfo),
                    profileContent = {userinfo -> DisplayUserInfo(userInfo,myProfile) },
                    content = {post -> ProfilePostCard(post) },
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        viewModel.onRefresh(userid)
                    },
                    lazyGridState = gridState,
                    tag = "profile"

                )
            }
        }
    }
}
