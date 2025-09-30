package com.appcoding.social

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.Functions.screenWidth
import com.appcoding.social.models.UserInfo
import com.appcoding.social.ui.theme.SocialTheme
import com.appcoding.social.models.PostResponse
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens


const val profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwLOO_ZbFmHlHIE2lpbitsU6v-528Ms3cWXA&s"
val myuser = UserInfo(1, "Atefeh","atefeh70",
    "a_zangooei90@yahoo.com","1234", "09171112233",
    profileImage, "It's Atefeh Zangooei", "My Link", 20, 25, 12)

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SocialTheme {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen(userid : Long = 0) {

    RightToLeftLayout {

        val profileImageSizeHalf = Dimens.profile_profile_image_size / 2
        val profilePaddingTop = Dimens.profile_header_size - profileImageSizeHalf
        val context = LocalContext.current

      /*  val initialUserId = runBlocking {
            UserPreferences.getUserIdFlow(context).first() ?: 0L
        }*/
        var myUserid by remember { mutableStateOf(1L) }

        var userInfo by remember { mutableStateOf<UserInfo?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var myProfile by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            try {
                myProfile = userid == myUserid
                userInfo = RetrofitInstance.api.getUserInfo(userid)
            }
            finally {
                isLoading = true
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(!isLoading) {
                Functions.myCircularProgress()
            }
            else{
                Username(userInfo!!.username)
                ProfileInfo(userInfo)
                Bio(userInfo!!.bio, userInfo!!.link)
                Spacer(modifier = Modifier.size(Dimens.normal_spacer))
                ProfileButtons(myProfile)
                Spacer(modifier = Modifier.size(Dimens.normal_spacer))
                DisplayPosts(userInfo!!.userid)
            }

        }

    }
}

@Composable
fun DisplayPosts(userid : Long) {
    var posts by remember { mutableStateOf<List<PostResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            posts = RetrofitInstance.api.getPostsByUserid(userid)

        }
        catch(e : Exception){
            Toast.makeText(context, e.toString(),Toast.LENGTH_SHORT).show()
        }
        finally {
            loading = true
        }
    }

    if(loading) {
        if (posts.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts) { item ->
                    ProfilePostCard(item)
                }
            }
        }
    }
    else{


    }
}


@Composable
fun ProfilePostCard(post : PostResponse){

    AsyncImage(model = ColorPainter(Color(android.graphics.Color.parseColor(post.image))),
        modifier = Modifier
        .padding(1.dp)
        .aspectRatio(1f),
        contentDescription = "post cover",
        contentScale = ContentScale.Crop
        )
}


@Composable
fun ProfileButtons(myProfile : Boolean){

    val followButtonWidth = screenWidth() /3


    //follow , message buttons Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.normal_padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if(!myProfile) {
            Button(
                onClick = {},
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
                    containerColor = Colors.appcolor,
                    contentColor = Color.White
                )

            ) {
                Text(
                    text = "دنبال کردن",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.size(Dimens.normal_spacer))

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
            Text(text = if(myProfile) "ویرایش پروفایل" else "پیام",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black)
        }

    }
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SocialTheme {

        ProfileScreen()
    }
}