package com.appcoding.social.screen.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.appcoding.social.R
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.screen.components.screenWidth
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.MainDataVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(post : PostResponse,
             navController: NavHostController,
             viewModel : MainDataVM) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var commentSheetState by remember { mutableStateOf(false) }


    RightToLeftLayout {

        Column(modifier = Modifier.wrapContentSize()) {

            Log.d("post","post image = ${post.image}")

            PostCard_ProfileInfo(post, navController)
            PostCard_LoadImage(post.image)

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
                    LikeButton(post, viewModel)

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = post.likeCount.toString(),
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
                        text = post.commentCount.toString(),
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
                        CommentBottomSheet(post.id, viewModel)
                    }
                }

                Image(painter = if (post.isSave)
                    painterResource(R.drawable.save_filled)
                else
                    painterResource(R.drawable.save),

                    contentDescription = "save",
                    modifier = Modifier
                        .size(Dimens.post_icons)
                        .clickable { viewModel.savePost(post.id) }
                )
            }

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
fun LikeButton(post : PostResponse,
               viewModel: MainDataVM){

    val scale = remember { Animatable(1f) }

    LaunchedEffect(post.isLike) {
        if (post.isLike) {
            scale.animateTo(1.5f, animationSpec = tween(150))
            scale.animateTo(
                1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        } else {
            scale.snapTo(1f)
        }
    }


    Image(painter = if (post.isLike)
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
                viewModel.likePost(post.id)
            }
    )

}


@Composable
fun PostCard_LoadImage(image: String) {
    AsyncImage(
                    model = image,
                    contentDescription = "postImage",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenWidth()),
                    contentScale = ContentScale.Crop
                )

   /* Box( modifier = Modifier
        .fillMaxWidth()
        .background(Color(android.graphics.Color.parseColor(image)))
        .height(screenWidth()))
*/

}


@Composable
fun PostCard_ProfileInfo(post : PostResponse, navController: NavHostController) {

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
                .clickable {},
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(Dimens.normal_spacer))

        Text(
            text = post.username,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                navController.navigate("profile/${post.userId}")
            }
        )
    }
}
