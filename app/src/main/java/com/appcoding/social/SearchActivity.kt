package com.appcoding.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.ui.theme.SocialTheme

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
fun SearchScreen() {

    RightToLeftLayout{

        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(Dimens.search_box_corner))
                .padding(Dimens.normal_padding)
                .background(Colors.search_box)
                .padding(horizontal = 20.dp, vertical = Dimens.normal_padding)
            ) {
                Icon(painter = painterResource(R.drawable.search),
                    contentDescription = "search",
                    modifier = Modifier.size(Dimens.search_box_icon_size))
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SocialTheme {
        SearchScreen()
    }
}