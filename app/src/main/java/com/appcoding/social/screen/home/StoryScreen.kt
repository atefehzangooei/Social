package com.appcoding.social.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.appcoding.social.R
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.models.UserStory
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.screenWidth
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.ui.theme.Irsans
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
                .border(width = 2.dp, color = Color.Magenta, CircleShape)
                //.padding(2.dp)
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
fun StoryPager(userid : Long, viewModel : MainDataVM){

    val stories by viewModel.stories.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {stories.size})


    LaunchedEffect(pagerState.currentPage) {
            delay(stories[pagerState.currentPage].duration.toLong())
            if (pagerState.currentPage < stories.lastIndex) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }


    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        StoryViewer(stories[page].userId)
    }

}

@Composable
fun StoryViewer(userid: Long){

    val viewModel : MainDataVM = hiltViewModel()
    val storyState by viewModel.storyState.collectAsState()
    val userStory by viewModel.userStory.collectAsState()

    val snackbarHostState = remember { SnackbarHostState()}
    val snackeScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        viewModel.getUserStory(userid)
    }

    LaunchedEffect(storyState.message) {
        if(storyState.message.isNotEmpty()){
            snackeScope.launch {
                snackbarHostState.showSnackbar(storyState.message)
            }
        }
    }

    var activeIndex by remember { mutableStateOf(0) }

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

                if(activeIndex< userStory.size){
                    Box(modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = userStory[activeIndex].image,
                            contentDescription = "story",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        ) {

                            activeIndex = (RowDurationProgress(
                                    userStory.size,
                                    userStory[0].duration * 1000L,
                                    activeIndex
                                )
                            )
                            RowUserInfo(userStory[0].profileImage, userStory[0].username)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowUserInfo(profileImage : String, username : String) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp)
    ){
        AsyncImage(model = profileImage,
            contentDescription = "profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Dimens.home_profile_image_size)
                .clip(CircleShape)
        )

        Text(text = username,
            style = TextStyle(
                    fontFamily = Irsans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    blurRadius = 6f
                )
            ),
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterVertically)
            )
    }
}


@Composable
fun RowDurationProgress(storyCount : Int,
                        duration : Long,
                        activeIndex : Int) : Int{  //duration = milliseconds

    var progress by remember { mutableFloatStateOf(0f) }  //float
    var activeIndexB = activeIndex

    LaunchedEffect(activeIndex) {
        if (activeIndex < storyCount) {
            progress = 0f
            val steps = 100
            val delayPerStep = duration / steps

            repeat(steps) {
                progress = it / steps.toFloat()
                delay(delayPerStep)
            }
            progress = 1f
            activeIndexB++


        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        repeat(storyCount) { index ->
            val barProgress =
                when {
                    index < activeIndex -> 1f // استوری‌های قبلی کاملاً پر
                    index == activeIndex -> progress // استوری فعلی در حال پخش
                    else -> 0f // استوری‌های بعدی خالی
                }

            LinearProgressIndicator(
                progress = { barProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .clip(RoundedCornerShape(50)),
                color = Color.White,
                trackColor = Color.Gray.copy(alpha = 0.5f),
            )
        }
    }
    return activeIndex
}


