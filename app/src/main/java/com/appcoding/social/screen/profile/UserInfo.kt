package com.appcoding.social.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.UserInfo
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens

@Composable
fun DisplayUserInfo(userInfo : UserInfo?, myProfile: Boolean){
    Column(modifier = Modifier.fillMaxWidth()) {
        Username(userInfo!!.username)
        ProfileInfo(userInfo)
        Bio(userInfo.bio, userInfo.link)
        Spacer(modifier = Modifier.size(Dimens.normal_spacer))
        ProfileButtons(myProfile)
        Spacer(modifier = Modifier.size(Dimens.normal_spacer))
    }
}


@Composable
fun ProfilePostCard(post : PostResponse){

    Image(painter = ColorPainter(Color(android.graphics.Color.parseColor(post.image))),
        modifier = Modifier
            .padding(1.dp)
            .aspectRatio(1f),
        contentDescription = "post cover",
        contentScale = ContentScale.Crop
    )
}



@Composable
fun ProfileButtons(myProfile : Boolean){

    //follow , message buttons Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.normal_padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {},
            shape = RoundedCornerShape(Dimens.button_corner),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(Dimens.button_corner),
                    ambientColor = Color.Black.copy(alpha = 0.25f),
                    spotColor = Color.Black.copy(alpha = 0.25f)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Colors.appcolor,
                contentColor = Color.White
            )

        ) {
            Text(
                text = if (myProfile) "ویرایش پروفایل" else "دنبال کردن",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }

    /* Spacer(modifier = Modifier.size(Dimens.normal_spacer))

     Button(onClick = {},
         shape = RoundedCornerShape(Dimens.profile_follow_button_corner),
         modifier = Modifier
             .width(followButtonWidth)
             .shadow(
                 elevation = 5.dp,
                 shape = RoundedCornerShape(Dimens.profile_follow_button_corner),
                 ambientColor = Color.Black.copy(alpha = 0.25f),
                 spotColor = Color.Black.copy(alpha = 0.25f)
             ),
         colors = ButtonDefaults.buttonColors(
             containerColor = Color.White,
             contentColor = Color.Black),
         border = BorderStroke(width = 1.dp, color = Colors.border_button)
     ) {
         Text(text = "ویرایش پروفایل",
             style = MaterialTheme.typography.bodyMedium,
             color = Color.Black)
     }

 }*/
}

@Composable
fun Bio(bio : String, link : String){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.normal_padding)
    ) {
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.profile_bio
        )

        TextButton(onClick = {}
        ) {
            Text(
                text = link,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun Username(username : String){

    Text(modifier = Modifier
        .fillMaxWidth()
        .padding(Dimens.profile_activity_padding),
        textAlign = TextAlign.Right,
        text = username,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun ProfileInfo(user : UserInfo?){

    RightToLeftLayout {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.profile_activity_padding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //profile image
            AsyncImage(
                model = user!!.profileImage,
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(Dimens.profile_profile_image_size)
                    .border(width = 3.dp, color = Color.White, shape = CircleShape)
            )

            //following - followers Row

            ProfileNumbers("پست ها", user.postCount)
            ProfileNumbers("دنبال کنندگان", user.following)
            ProfileNumbers("دنبال شوندگان", user.follower)
        }

    }
}

@Composable
fun ProfileNumbers(title : String, number: Int)
{
    Column(modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = number.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black)


        Text(text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black)

    }
}