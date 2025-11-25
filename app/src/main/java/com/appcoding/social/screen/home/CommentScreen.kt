package com.appcoding.social.screen.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.screen.components.SendProgress
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.MainDataVM

@Composable
fun CommentBottomSheet(postId : Long,
                       viewModel: MainDataVM
){

    val comments by viewModel.comments.collectAsState()
    val commentState by viewModel.commentState.collectAsState()
    val newComment by viewModel.newComment.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()


    LaunchedEffect(Unit){
        viewModel.getComments(postId)
    }

    RightToLeftLayout {

        if(commentState.isLoading){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center)
            {
                LoadingDataProgress()
            }
        }
        else {

            Column(modifier = Modifier.fillMaxSize()) {
                if(comments.size > 0){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(comments) { comment ->
                        CommentCard(comment)
                    }
                }
            }
                else{
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        contentAlignment = Alignment.Center)
                    {
                        Text(text = "نظری ثبت نشده است !",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black)
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
                    //val userProfile = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTu8Qi_EGuDWDLusHz6fyxhgaQWa6q0YsOiBH3adnqLx-6_JbLy_-ch2P3xcDxtTh-g9qY&usqp=CAU"

                    AsyncImage(
                        model = profileImage,
                        contentDescription = "my profile",
                        modifier = Modifier
                            //.weight(1f)
                            .size(Dimens.comment_user_profile)
                            .clip(CircleShape)
                            .align(Alignment.CenterVertically)
                    )

                    TextField(
                        value = newComment,
                        onValueChange = viewModel::onCommentChanged ,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        placeholder = { Text("نظرت رو بگو") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(5f),
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

                    Box(modifier = Modifier
                        .weight(1f)
                        .size(Dimens.comment_user_profile),
                        contentAlignment = Alignment.Center)
                    {
                        if (commentState.isUploading) {
                            SendProgress()
                        } else {
                            Icon(imageVector = Icons.Filled.Check,
                                contentDescription = "send comment",
                                tint = Colors.appcolor,

                                modifier = Modifier
                                    //.weight(1f)
                                   // .size(Dimens.comment_user_profile)
                                    .clip(CircleShape)
                                   // .align(Alignment.CenterVertically)
                                    .clickable {
                                        if (newComment.isNotEmpty()) {
                                            viewModel.sendComment(postId)
                                        }
                                    }
                            )
                        }
                    }
                }
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

