package com.appcoding.social

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.Functions.screenWidth
import com.appcoding.social.models.UserInfo
import com.appcoding.social.ui.theme.SocialTheme
import com.appcoding.social.models.PostResponse
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.ProfileScreenVM
import kotlinx.coroutines.launch


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
fun ProfileScreen(userid : Long = 1) {

    
    RightToLeftLayout {
        
        val viewModel : ProfileScreenVM = hiltViewModel()
        
        val userInfo by viewModel.userInfo.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val message by viewModel.message.collectAsState()
        val myProfile by viewModel.myProfile.collectAsState()
        val success by viewModel.success.collectAsState()

        val scrollState = rememberScrollState()

        LaunchedEffect(Unit) {
           viewModel.getProfile(userid)
        }


        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading)
                Functions.myCircularProgress()

            if (success) {
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

    val viewModel: ProfileScreenVM = hiltViewModel()

    val posts by viewModel.userPosts.collectAsState()
    val postLoading by viewModel.postLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val postSuccess by viewModel.postSuccess.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.getUserPosts(userid)

        if(message.isNotEmpty()){
            snackScope.launch {
                snackbarHostState.showSnackbar(message)
            }

        }
    }

        Box(modifier = Modifier
            .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if(postLoading)
                 Functions.myCircularProgress()

            if(postSuccess) {
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SocialTheme {

        ProfileScreen()
    }
}