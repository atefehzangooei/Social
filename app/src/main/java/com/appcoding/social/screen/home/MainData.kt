package com.appcoding.social.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.PullToRefreshLazyList
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.MainDataVM
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@SuppressLint("ShowToast", "FrequentlyChangedStateReadInComposition")
@Composable
fun MainData(navController: NavHostController) {

    val parentEntry = remember(navController.currentBackStackEntry){
        navController.getBackStackEntry("nav_graph")
    }

    val viewModel: MainDataVM = hiltViewModel(parentEntry)

    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val postState by viewModel.postState.collectAsState()
    val storyState by viewModel.storyState.collectAsState()
    val userid by viewModel.userid.collectAsState()
    val lastSeenId by viewModel.lastSeenId.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()

    val listState = rememberLazyListState()
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getFirst()
    }

    LaunchedEffect(postState.message) {
        if (postState.message.isNotEmpty()) {
            snackScope.launch {
                snackbarHostState.showSnackbar(postState.message)
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->

                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalItems = layoutInfo.totalItemsCount

                if (!postState.isLoadMore && lastVisibleItem != null && lastVisibleItem >= totalItems - 1) {
                    viewModel.getDataMore(lastSeenId)
                }
            }
    }

    if (postState.success || storyState.success) {
        PullToRefreshLazyList(
            posts = posts,
            extraList = stories,
            extraContent = { story -> StoryCard(story, userid, navController) },
            content = { post, index -> PostCard(post, navController, viewModel) },
            isRefreshing = postState.isRefreshing,
            onRefresh = {
                viewModel.onRefresh()
            },
            lazyListState = listState,
            tag = "main"
        )
    }

    RightToLeftLayout {

        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { contentPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.background)
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            )
            {
                if (postState.isLoading) LoadingDataProgress()
            }
        }
    }
}


