package com.appcoding.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.SearchRequest
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
fun SearchScreen() {

    var searchText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var isTyping by remember { mutableStateOf(false) }
    var searchAction by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    var posts by remember { mutableStateOf<List<PostResponse>>(emptyList()) }


    RightToLeftLayout{

        Scaffold( snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
        ) {contentPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Colors.background)
            .padding(Dimens.normal_padding)
        ) {
            TextField(value = searchText,
                onValueChange = {
                    searchText = it
                    if(searchText.isBlank()) {
                        isTyping = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {focuseState ->
                        isFocused = focuseState.isFocused
                    },
                shape = RoundedCornerShape(Dimens.search_box_corner),
                textStyle = MaterialTheme.typography.bodyMedium,
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.search),
                        contentDescription = "search",
                        modifier = Modifier.size(20.dp),
                        tint = Colors.search_icon)
                },
                trailingIcon = {
                    if(isFocused || isTyping) {
                        IconButton(onClick = {
                            isFocused = false
                            searchText = ""
                            isTyping = false
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = "close search",
                                modifier = Modifier.size(20.dp),
                                tint = Colors.search_icon
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Colors.placeholder,
                    unfocusedPlaceholderColor = Colors.placeholder,
                    focusedContainerColor = Colors.search_box,
                    unfocusedContainerColor = Colors.search_box

                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchAction = true
                    }
                )
            )

            Spacer(modifier = Modifier.size(10.dp))

            if(searchAction) {
                Functions.myCircularProgress()
                LaunchedEffect(searchAction) {
                    try {
                        posts = RetrofitInstance.api.searchPost(
                            SearchRequest(
                                userId = ,
                                text = searchText
                            )
                        )

                    } catch (e: Exception) {
                        snackScope.launch {
                            snackbarHostState.showSnackbar("خطا در اتصال")
                        }
                    } finally {
                        searchAction = false
                    }
                }
            }
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