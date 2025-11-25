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
import androidx.navigation.NavHostController
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.PullToRefreshLazyList
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.viewmodel.MainDataVM
import com.appcoding.social.viewmodel.ProfileScreenVM
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(userid : Long,
                  navController: NavHostController) {

    RightToLeftLayout {

        val parentEntry = remember(navController.currentBackStackEntry) {
            navController.getBackStackEntry("nav_graph")
        }

        val viewModel : ProfileScreenVM = hiltViewModel(parentEntry)

        val userInfo by viewModel.userInfo.collectAsState()
        val posts by viewModel.userPosts.collectAsState()
        val myProfile by viewModel.myProfile.collectAsState()
        val state by viewModel.state.collectAsState()
        val postState by viewModel.postState.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        val snackScope = rememberCoroutineScope()
        val gridState = rememberLazyGridState()

        LaunchedEffect(Unit) {
            viewModel.getProfile(userid)
        }

        LaunchedEffect(state.message, postState.message) {
            if(state.message.isNotEmpty()){
                snackScope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
            }
            if(postState.message.isNotEmpty()){
                snackScope.launch {
                    snackbarHostState.showSnackbar(postState.message)
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
            if ((state.isLoading && postState.isLoading) || state.isRefreshing)
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    LoadingDataProgress()
                }

            if (state.success && postState.success) {

                PullToRefreshLazyList(
                    posts = posts,
                    extraList = listOf(userInfo),
                    extraContent = {userinfo -> DisplayUserInfo(userInfo, myProfile) },
                    content = { post, index -> ProfilePostCard(post, index, navController) },
                    isRefreshing = state.isRefreshing,
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
