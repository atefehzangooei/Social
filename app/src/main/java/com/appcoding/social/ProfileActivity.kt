package com.appcoding.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.Functions.screenWidth
import com.appcoding.social.models.User
import com.appcoding.social.ui.theme.Purple40
import com.appcoding.social.ui.theme.SocialTheme
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ConstrainScope




const val profileImage = "https://probug.ir/social/images/bob.jpg"
val myuser = User(1, "Atefeh","atefeh70",
    "a_zangooei90@yahoo.com","1234", "09171112233",
    profileImage, "It's Atefeh Zangooei", "My Link", 20, 25, 12)

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SocialTheme {
                ProfileScreen(myuser)
            }
        }
    }
}

@Composable
fun ProfileScreen(user : User) {

    RightToLeftLayout{


     /*   val posts = staticPostData()
        val scrollState = rememberScrollState(0)

        val myModifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Dimens.normal_padding)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(Dimens.normal_padding)
            .verticalScroll(state = scrollState)
        ){
            Row(modifier = myModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AsyncImage(model = profileImage,
                    contentDescription = "profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(Dimens.profile_profile_image)
                        .clip(CircleShape)

                    )

                ProfileCount("دنبال کنندگان", user.following)

                ProfileCount("دنبال شوندگان", user.follower)

                ProfileCount("پست ها", user.postCount)

            }


                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = user.link,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

            //buttons Row
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Dimens.normal_padding)
            ) {
                OutlinedButton(onClick = {},
                    shape = RoundedCornerShape(Dimens.button_corner),
                    border = BorderStroke(width = Dimens.button_border, color = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "ویرایش پروفایل",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.size(Dimens.normal_spacer))

                OutlinedButton(onClick = {},
                    shape = RoundedCornerShape(Dimens.button_corner),
                    border = BorderStroke(width = Dimens.button_border, color = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "ویرایش پروفایل",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold)
                }
            }

            //highlight Row
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
            ){  }

            //display posts
            LazyVerticalGrid(modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(3)
            ) {
                items(posts){post->

                }
            }


        }*/

        val profileImageSizeHalf = Dimens.profile_profile_image_size / 2
        val headerSize = Dimens.profile_header_size
        val profilePaddingTop = Dimens.profile_header_size - profileImageSizeHalf

        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
        ) {
            val (headerRef, profileImageRef, contentRef) = createRefs()

            //header
            Image(painter = painterResource(R.drawable.image),
                contentDescription = "header",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.profile_header_size)
                    .constrainAs(headerRef){
                        top.linkTo(parent.top)
                    })


            Column(modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        Dimens.profile_bottom_design_corner,
                        Dimens.profile_bottom_design_corner, 0.dp, 0.dp
                    )
                )
                .constrainAs(contentRef){
                    top.linkTo(headerRef.bottom)
                }
            ){
                BottomDesign(user, profileImageSizeHalf)
            }

           Box(modifier = Modifier
               .fillMaxWidth()
               .padding(profilePaddingTop),
               contentAlignment = Alignment.TopCenter
           )
           {

               //profile image
               AsyncImage(model = user.profileImage,
                   contentDescription = "profile image",
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .clip(CircleShape)
                       .size(Dimens.profile_profile_image_size)
                       .border(width = 3.dp, color = Color.White, shape = CircleShape)
               )
           }



        }

    }
}

@Composable
fun BottomDesign(user : User, rowHeight : Dp){

    val followButtonWidth = screenWidth() /3

    RightToLeftLayout {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.background)
                .padding(Dimens.normal_padding)
        )
        {

            //following - followers Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight)
                    .padding(Dimens.normal_padding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                ProfileNumbers("دنبال کنندگان", user.following)
                ProfileNumbers("دنبال شوندگان", user.follower)
            }


            //username Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.normal_padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }


            //bio Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.profile_bio_padding, 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.profile_bio
                )
            }

            //follow , message buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.normal_padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
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
                       containerColor = Colors.appcolor,
                       contentColor = Color.White)

               ) {
                   Text(text = "دنبال کردن",
                       style = MaterialTheme.typography.bodyMedium,
                       color = Color.White)
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
                    border = BorderStroke(width = 1.dp,
                        color = Colors.border_button)
                ) {
                    Text(text = "پیام",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black)
                }

            }

            //posts

        }
    }
}


@Composable
fun ProfilePostCard(){

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

        ProfileScreen(myuser)
    }
}