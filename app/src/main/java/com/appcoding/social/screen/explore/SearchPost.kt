package com.appcoding.social.screen.explore

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.appcoding.social.DisplayStaggeredList
import com.appcoding.social.R
import com.appcoding.social.data.api.RetrofitInstance
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.SearchPostVM
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchPost(userid : Long) {

    val viewModel : SearchPostVM = hiltViewModel()

    val searchText by viewModel.searchText.collectAsState()
    val isFocused by viewModel.isFocused.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    val searchAction by viewModel.searchAction.collectAsState()
    var lastSeenId by remember { mutableStateOf(-1L) }
    var posts by remember { mutableStateOf<List<PostResponse>>(emptyList()) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()



    RightToLeftLayout{

        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Colors.background)
            ) {
                TextField(value = searchText,
                    onValueChange = {
                        searchText = it
                        isTyping = true

                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.normal_padding)
                        .onFocusChanged { focuseState ->
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
                            keyboardController?.hide()
                        }
                    )
                )

                Spacer(modifier = Modifier.size(10.dp))


                LaunchedEffect(searchAction) {

                    if(searchAction) {
                        try {
                            posts = RetrofitInstance.api.searchPost(
                                text = searchText,
                                userId = userid,
                                lastSeenId = lastSeenId,
                                size = size
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


                if(posts.isNotEmpty()){
                    DisplayStaggeredList(posts, )
                }

                if(searchAction) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Functions.myCircularProgress()
                    }
                }
            }
        }
    }
}
