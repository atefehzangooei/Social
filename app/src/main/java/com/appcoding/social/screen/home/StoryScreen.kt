package com.appcoding.social.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appcoding.social.R
import com.appcoding.social.models.StoryResponse
import com.appcoding.social.ui.theme.Dimens

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
