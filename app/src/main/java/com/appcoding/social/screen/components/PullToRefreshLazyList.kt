package com.appcoding.social.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.viewmodel.MainDataVM
import com.appcoding.social.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T,U> PullToRefreshLazyList(
    posts : List<T>,
    extraList : List<U>,
    extraContent : @Composable (U) -> Unit,
    content : @Composable (T, Int) -> Unit,
    isRefreshing : Boolean,
    onRefresh : () -> Unit,
    lazyListState : LazyListState? = null,
    lazyGridState : LazyGridState? = null,
    tag : String,
    addPostState : UiState? = null
){
    val pullToRefreshState = rememberPullToRefreshState()
    val progressScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ){
        when(tag){
            "main" -> {
                LazyColumn(
                    state = lazyListState!!,
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            items(extraList) {
                                extraContent(it)
                            }
                        }
                    }

                    item {
                        if(addPostState!!.isUploading)
                            UploadPostProgress(addPostState)
                    }

                    items(posts) {
                        content(it, 0)
                    }
                }
            }
            "profile" -> {
                LazyVerticalGrid(
                    state = lazyGridState!!,
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(1.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        extraContent(extraList[0])
                    }

                    itemsIndexed(posts) {index, item ->
                        content(item, index)
                        }
                }
            }
            "search" -> {

            }
        }


        if(pullToRefreshState.isRefreshing){

            PullToRefreshContainer(state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter))
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if(isRefreshing){
                pullToRefreshState.startRefresh()
            }
            else{
                pullToRefreshState.endRefresh()
            }
        }



    }
}

@Composable
fun UploadPostProgress(state : UiState){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight())
    {
        LinearProgressIndicator(
            progress = { state.progress / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center),
            trackColor = Colors.appcolor

        )
    }
}