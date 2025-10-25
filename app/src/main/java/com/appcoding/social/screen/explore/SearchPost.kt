package com.appcoding.social.screen.explore

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.appcoding.social.R
import com.appcoding.social.models.PostResponse
import com.appcoding.social.screen.components.LoadingDataProgress
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.SearchPostVM
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchPost() {

    val viewModel : SearchPostVM = hiltViewModel()

    val searchText by viewModel.searchText.collectAsState()
    val isFocused by viewModel.isFocused.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    val searchAction by viewModel.searchAction.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val success by viewModel.success.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()

    DisplayStaggeredList(posts)

    RightToLeftLayout {

    LaunchedEffect(searchAction) {
        if(searchAction) {
            viewModel.getPosts()
        }
    }

    LaunchedEffect(message) {
            if(message.isNotEmpty()){
                snackScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }

        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo }
                .collect { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    val totalItems = layoutInfo.totalItemsCount

                    if (lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItems - 1) {
                        viewModel.getPosts()
                    }
                }
        }


        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Colors.background)
            ) {
                TextField(value = searchText,
                    onValueChange = viewModel::onSearchTextChanged,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.normal_padding)
                        .onFocusChanged { focusState ->
                            viewModel.onFocusChanged(focusState)},
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
                            IconButton(onClick = viewModel::closeSearch) {
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
                           viewModel.keyboardSearchAction()
                            keyboardController?.hide()
                        }
                    )
                )

                Spacer(modifier = Modifier.size(10.dp))

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if(isLoading) {
                            LoadingDataProgress()
                        }

                        if(success){
                            DisplayStaggeredList(posts)
                        }
                    }
            }
        }
    }
}


@Composable
fun DisplayStaggeredList(posts : List<PostResponse>){

    LazyVerticalStaggeredGrid (modifier = Modifier
        .fillMaxSize(),
        columns = StaggeredGridCells.Fixed(3),
    ) {
        itemsIndexed(items = posts
        ){ index  ,post ->
            /* AsyncImage(
                 model = ColorPainter(Color(android.graphics.Color.parseColor(post.image))),
                 contentDescription = "post cover",
                 modifier = Modifier
                     .fillMaxWidth()
                     .border(width = 1.dp, color = Color.White)
                     .aspectRatio(
                         if(index % 10 in listOf(0,7)) 0.5f else 1f
                     ),
                 contentScale = ContentScale.Crop
             )*/

            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color(android.graphics.Color.parseColor(post.image)))
                .border(width = 1.dp, color = Color.White)
                .aspectRatio(
                    if(index % 10 in listOf(0,7)) 0.5f else 1f
                )
                .clickable {  }

            )

        }
    }

}

