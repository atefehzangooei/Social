package com.appcoding.social

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.appcoding.social.models.StoryResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T,U> PullToRefreshLazyList(
    posts : List<T>,
    extraList : List<U>,
    extraContent : @Composable (U) -> Unit,
    content : @Composable (T) -> Unit,
    isRefreshing : Boolean,
    onRefresh : () -> Unit,
    lazyListState : LazyListState
){
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ){

            LazyColumn(
                state = lazyListState,
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


                items(posts) {
                    content(it)
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