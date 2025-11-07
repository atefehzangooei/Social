package com.appcoding.social.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.appcoding.social.R
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.MainDataVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StoryCard(story : StoryResponse, userid : Long, navController: NavHostController){

    Box(modifier = Modifier
        .wrapContentSize()
        .padding(end = Dimens.home_padding_between_story)
    ) {
        Image(painter = painterResource(R.drawable.no_image),
            contentDescription = "user story",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Dimens.story_home_display)
                .clip(CircleShape)
                .padding(1.dp)
                .border(width = 2.dp, color = Color.Magenta, CircleShape)
                .clickable {
                    navController.navigate("story/${story.userId}")
                })
        if(story.userId == userid){
            Icon(imageVector = Icons.Filled.AddCircle,
                contentDescription = "add story",
                modifier = Modifier
                    .size(Dimens.story_home_add)
                    .border(width = 2.dp, color = Color.White, shape = CircleShape)
                    .align(Alignment.BottomEnd))
        }
    }

}


@Composable
fun StoryViewer(userid: Long){

    val viewModel : MainDataVM = hiltViewModel()
    val storyState by viewModel.storyState.collectAsState()
    val userStory by viewModel.userStory.collectAsState()

    val snackbarHostState = remember { SnackbarHostState()}
    val snackeScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {userStory.size})

    LaunchedEffect(Unit){
        viewModel.getUserStory(userid)
    }


    LaunchedEffect(pagerState.currentPage) {
        if(storyState.success) {
            delay(userStory[pagerState.currentPage].duration.toLong())
            if (pagerState.currentPage < userStory.lastIndex) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    LaunchedEffect(storyState.message) {
        if(storyState.message.isNotEmpty()){
            snackeScope.launch {
                snackbarHostState.showSnackbar(storyState.message)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState)})
    { contentpadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentpadding)
                .background(Colors.story_background),
            contentAlignment = Alignment.Center
        )
        {
            if (storyState.isLoading) {
                LoadingDataProgress()
            } else if (storyState.success) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = userStory[page].image,
                        contentDescription = "story",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}