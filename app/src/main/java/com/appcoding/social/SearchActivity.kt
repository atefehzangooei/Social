package com.appcoding.social

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.ui.theme.SocialTheme
import kotlinx.coroutines.launch

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialTheme {

            }
        }
    }
}



@Composable
fun DisplaySearchPosts(index : Int, searchText : String){

    RightToLeftLayout {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.search_view_post_background)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.display_search_padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "search",
                    tint = Color.White,
                    modifier = Modifier.size(Dimens.display_search_icon)
                )

                Row(
                    modifier = Modifier
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = searchText,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "back",
                        tint = Color.White,
                        modifier = Modifier.size(Dimens.display_search_icon)
                    )
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

           /* AsyncImage(
                model = post.image,
                contentDescription = "post cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.White)
                    .aspectRatio(
                        if (index % 10 in listOf(0, 7)) 0.5f else 1f
                    ),
                contentScale = ContentScale.Crop
            )*/
            
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .aspectRatio(1f)
                //.background(Color(android.graphics.Color.parseColor(post.image))),
                )


        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SocialTheme {
        DisplaySearchPosts(0, "برنامه نویسی")
    }
}