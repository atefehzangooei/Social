package com.appcoding.social.screen.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.Functions.screenWidth
import com.appcoding.social.R
import com.appcoding.social.data.api.RetrofitInstance
import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.screen.components.MyProgress
import com.appcoding.social.screen.components.PullToRefreshLazyList
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.MainDataVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("ShowToast", "FrequentlyChangedStateReadInComposition")
@Composable
fun MainData(userid : Long, navController: NavHostController) {

    val viewModel : MainDataVM = hiltViewModel()

    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getFirst()
    }

    LaunchedEffect(listState) {
        snapshotFlow {listState.layoutInfo}
            .collect{ layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalItems = layoutInfo.totalItemsCount

                if (!isLoading && lastVisibleItem != null && lastVisibleItem >= totalItems - 1) {
                    viewModel.getData()
                }
            }
    }


    PullToRefreshLazyList(
        posts = posts,
        extraList = stories,
        extraContent = { story -> StoryCard(story, userid, navController) },
        content = { post -> PostCard(post, userid, navController) },
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.onRefresh()
        },
        lazyListState = listState,
        tag = "main"
    )


    if(isLoading){
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center)
        {
            MyProgress()
        }
    }
}


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
                .clickable {
                    navController.navigate("story")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(post : PostResponse, userid : Long, navController: NavHostController) {

    var commentSheetState by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var liked by remember { mutableStateOf(post.isLike) }
    var saved by remember { mutableStateOf(post.isSave) }


    val likeScope = rememberCoroutineScope()
    val saveScope = rememberCoroutineScope()
    var likeCount by remember { mutableStateOf(post.likeCount) }
    val commentCount by remember { mutableStateOf(post.commentCount) }


    RightToLeftLayout {

        Column(modifier = Modifier.wrapContentSize()) {

            PostCard_ProfileInfo(post, navController)
            PostCard_LoadImage(post.image)
            PostCard_LikeRow(liked)


            Spacer(modifier = Modifier.size(Dimens.normal_spacer))

            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(Dimens.normal_padding)
            )


        }
    }
}

@Composable
fun PostCard_LikeRow(liked : Boolean){
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val scale = remember { Animatable(1f) }


            LaunchedEffect(liked) {
                if (liked) {

                    scale.animateTo(1.5f, animationSpec = tween(150))
                    scale.animateTo(
                        1f,
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    )
                } else {

                    scale.snapTo(1f)
                }
            }



            Image(painter = if (liked)
                painterResource(R.drawable.red_heart)
            else
                painterResource(R.drawable.like),
                contentDescription = "like",
                modifier = Modifier
                    .size(Dimens.post_icons)
                    .indication(interactionSource = remember { MutableInteractionSource() },
                        indication = null)
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
                    .clickable {
                        liked = !liked
                        likeScope.launch {
                            if (liked) {
                                likeCount++
                                val response = RetrofitInstance.api.likePost(
                                    LikeRequest(
                                        postId = post.id,
                                        userId = userid,
                                        date = "",
                                        time = ""
                                    )
                                )
                            } else {
                                likeCount--
                                val response = RetrofitInstance.api.disLikePost(
                                    postId = post.id,
                                    userId = userid
                                )
                            }
                        }
                    }
            )


            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = likeCount.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(15.dp))

            Image(painter = painterResource(R.drawable.comment),
                contentDescription = "comment",
                modifier = Modifier
                    .size(Dimens.post_icons)
                    .clickable { commentSheetState = true }
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = commentCount.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(15.dp))

            Image(painter = painterResource(R.drawable.share),
                contentDescription = "share",
                modifier = Modifier
                    .size(Dimens.post_icons)
                    .clickable {}
            )
        }


        if (commentSheetState) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { commentSheetState = false },
                //modifier = Modifier.height(300.dp),
                containerColor = Color.White,
                scrimColor = Color.Black.copy(alpha = 0.5f),
                //dragHandle = {BottomSheetDefaults.DragHandle()}
            ) {
                CommentBottomSheet(post, userid)
            }
        }

        Image(painter = if (saved)
            painterResource(R.drawable.save_filled)
        else
            painterResource(R.drawable.save),

            contentDescription = "save",
            modifier = Modifier
                .size(Dimens.post_icons)
                .clickable {
                    saved = !saved
                    saveScope.launch {
                        if(saved){
                            val response = RetrofitInstance.api.savePost(
                                SavePostRequest(
                                    userId = userid,
                                    postId = post.id,
                                    date = "14",
                                    time = "14"
                                )
                            )
                        }
                        else{
                            val response = RetrofitInstance.api.unSavePost(
                                userId = userid,
                                postId = post.id
                            )
                        }
                    }
                }

        )

    }
}


@Composable
fun PostCard_LoadImage(image: String) {
    /*AsyncImage(
                    model = ColorPainter(Color(android.graphics.Color.parseColor(post.image))),
                    contentDescription = "postImage",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenWidth()),
                    contentScale = ContentScale.Crop
                )*/

    Box( modifier = Modifier
        .fillMaxWidth()
        .background(Color(android.graphics.Color.parseColor(image)))
        .height(screenWidth()))


}


@Composable
fun PostCard_ProfileInfo(post : Post, navController: NavHostController) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Dimens.normal_padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    )
    {
        AsyncImage(
            model = post.userProfile,
            contentDescription = "home_profile_image",
            modifier = Modifier
                .size(Dimens.home_profile_image_size)
                .clip(CircleShape)
                .clickable {
                    navController.navigate("profile/${post.userId}")
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(Dimens.normal_spacer))

        Text(
            text = post.username,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun CommentBottomSheet(post : PostResponse, userid : Long){

    val context = LocalContext.current
    var comments by remember { mutableStateOf<List<CommentResponse>>(emptyList()) }
    var newComment by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    // val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit){
        //focusRequester.requestFocus()
        try{
            comments = RetrofitInstance.api.getComments(post.id)
            post.commentCount++
        }
        catch (ex : Exception){
            Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()
        }
    }

    RightToLeftLayout {

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(comments) { comment ->
                    CommentCard(comment)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .wrapContentHeight()
                    .padding(Dimens.normal_padding)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )
                    },
                verticalAlignment = Alignment.Bottom
            ) {
                val userProfile =
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTu8Qi_EGuDWDLusHz6fyxhgaQWa6q0YsOiBH3adnqLx-6_JbLy_-ch2P3xcDxtTh-g9qY&usqp=CAU"

                AsyncImage(
                    model = userProfile,
                    contentDescription = "my profile",
                    modifier = Modifier
                        //.weight(1f)
                        .size(Dimens.comment_user_profile)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)

                )

                TextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("نظرت رو بگو") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(5f),
                    // .focusRequester(focusRequester),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Colors.placeholder,
                        unfocusedPlaceholderColor = Colors.placeholder

                    )
                )

                Icon(imageVector = Icons.Filled.Check,
                    contentDescription = "send comment",
                    tint = Colors.appcolor,
                    modifier = Modifier
                        .weight(1f)
                        .size(Dimens.comment_user_profile)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            if (newComment.isNotEmpty()) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val commentRequest = CommentRequest(
                                        postId = post.id,
                                        userId = userid,
                                        comment = newComment,
                                        date = "1404",
                                        time = "18:00"
                                    )
                                    val addedComment =
                                        RetrofitInstance.api.addComment(commentRequest)
                                    comments = comments + addedComment
                                    newComment = ""
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun CommentCard(comment: CommentResponse) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(Dimens.normal_padding)
    ) {
        AsyncImage(model = comment.userProfile,
            contentDescription = "user profile",
            modifier = Modifier
                .size(Dimens.comment_user_profile)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(modifier = Modifier
            .wrapContentSize()
        ) {
            Text(text = comment.username,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black)

            Text(text = comment.comment,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black)
        }
    }

}

