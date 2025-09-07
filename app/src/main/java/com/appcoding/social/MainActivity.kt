package com.appcoding.social

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.Functions.screenWidth
import com.appcoding.social.Functions.uriToFile
import com.appcoding.social.data.ApiService
import com.appcoding.social.models.CommentRequest
import com.appcoding.social.models.CommentResponse
import com.appcoding.social.models.LikeRequest
import com.appcoding.social.models.PostResponse
import com.appcoding.social.models.SavePostRequest
import com.appcoding.social.ui.theme.SocialTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SocialTheme {

            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MyApp(userid : Long, navController : NavHostController) {

    var selectedIndex by remember { mutableStateOf(0) }
    var isAddScreenVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

   /* val initialUserId = runBlocking {
        UserPreferences.getUserIdFlow(context).first() ?: 0L
    }*/


    val navigationItems = listOf(
        NavigationItem("Home",
            painterResource(R.drawable.home),
            painterResource(R.drawable.home_selected)
        ),
        NavigationItem("Search",
            painterResource(R.drawable.search),
            painterResource(R.drawable.search_selected)
        ),
        NavigationItem("Add",
            painterResource(R.drawable.add),
            painterResource(R.drawable.add)
        ),
        NavigationItem("Reels",
            painterResource(R.drawable.reel),
            painterResource(R.drawable.reel_selected)
        ),
        NavigationItem("Profile",
            painterResource(R.drawable.profile),
            painterResource(R.drawable.profile_selected)
        )
        
    )

    RightToLeftLayout{

        val btnShape = RoundedCornerShape(Dimens.bottom_navigation_corner,
            Dimens.bottom_navigation_corner, 0.dp, 0.dp)

        Scaffold(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),

            bottomBar = {
                if (!isAddScreenVisible) {
                    BottomAppBar(
                        containerColor = Colors.bottom_appbar_container,
                        modifier = Modifier
                            .wrapContentHeight()
                            .background(Color.White)
                            .shadow(elevation = Dimens.bottom_appbar_shadow)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(Color.White),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            navigationItems.forEachIndexed { index, item ->
                                IconButton(onClick = {
                                    if (index == 2) {
                                        isAddScreenVisible = true
                                    } else {
                                        selectedIndex = index
                                    }
                                })
                                {
                                    Icon(
                                        painter =
                                        if (selectedIndex == index)
                                            item.selected
                                        else item.unselected,
                                        contentDescription = "icon",
                                        modifier = Modifier.size(Dimens.bottom_appbar_size)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
        {contentPadding ->

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(color = Colors.background),
                contentAlignment = Alignment.Center)
            {
                if(userid == 0L){
                    Functions.myCircularProgress()
                }
                else {
                    when (selectedIndex) {
                        0 -> HomeScreen(userid, navController)
                        1 -> SearchScreen(userid)
                        3 -> ReelsScreen()
                        4 -> ProfileScreen(userid)
                    }
                }

                AnimatedVisibility(
                    visible = isAddScreenVisible,
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    )
                ){
                    AddPostScreen(onBack = {isAddScreenVisible = false})
                    }
            }
        }
    }

}


@Composable
fun ReelsScreen() {

}

@Composable
fun AddPostScreenNext(onBack: () -> Unit, selectedImageUri: Uri?){

    val context = LocalContext.current
    val imageSize = screenWidth() / 2 +20.dp
    var neveshtak by remember { mutableStateOf("") }

    var isUploading by remember { mutableStateOf(false) }
    val apiService = RetrofitInstance.api
    var toastMessage by remember { mutableStateOf("") }
    var uploadComplete by remember { mutableStateOf(false) }


    BackHandler(enabled = true) {
        onBack()
    }

    RightToLeftLayout {

        Box(modifier = Modifier.fillMaxSize()
            .background(color = Color.White),
            contentAlignment = Alignment.BottomCenter)
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.normal_padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            )
            {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.normal_padding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "پست جدید",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "BackToAddPostScreen",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onBack() })
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "ImagePost",
                        modifier = Modifier
                            .size(imageSize),
                        contentScale = ContentScale.Crop
                    )
                }

                TextField(
                    value = neveshtak,
                    onValueChange = { neveshtak = it },
                    placeholder = { Text("یه نوشتک قشنگ بذار") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Colors.placeholder,
                        unfocusedPlaceholderColor = Colors.placeholder
                    )

                )
            }


            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height((0.5).dp)
                    .background(Colors.line))

                Button(onClick = {
                    if(neveshtak.isNotEmpty() && selectedImageUri != null) {
                        isUploading = true
                        selectedImageUri.let {safeUri ->
                            val imageFile = uriToFile(safeUri, context)
                            uploadPost(neveshtak, imageFile, apiService) { success, message ->
                                isUploading = false
                                uploadComplete = true
                                toastMessage = message
                               /* if (success)
                                    toastMessage = "موفقیت آمیز بود"
                                else
                                    toastMessage = "خطا در آپلود"
*/
                            }
                        }

                    }

                },
                    modifier = Modifier.width(imageSize),
                    shape = RoundedCornerShape(Dimens.button_corner),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Colors.add_post_button
                    )) {
                    Text("اشتراک گذاری")
                }
            }


        }
    }

    LaunchedEffect(toastMessage){
        if(uploadComplete) {
            neveshtak = toastMessage
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            uploadComplete = false
        }
    }
}

private fun uploadPost(neveshtak : String , imageFile : File,
                       apiService : ApiService, callback : (Boolean, String) -> Unit){
    val textPart = neveshtak.toRequestBody("text/plain".toMediaTypeOrNull())
    val imagePart = MultipartBody.Part.createFormData(
        "image",
        imageFile.name,
        imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    )

    apiService.addPost(textPart, imagePart).enqueue(object : retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if(response.isSuccessful){
                callback(true, "upload successfully")
            }
            else {
                if(response.body()!=null) {
                        callback(false, response.body()!!)
                }
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            callback(false, "message is : ${t.message}")

        }
    })
}


@Composable
fun  AddPostScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    var images by remember { mutableStateOf(emptyList<Uri>()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var next by remember { mutableStateOf(false) }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            images = getGalleryImages(context)
            selectedImageUri = images[0]
        }
    }


    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }


    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            images = getGalleryImages(context)
            selectedImageUri = images[0]
        } else {
            permissionLauncher.launch(permission)
        }

    }

    BackHandler(enabled = true) {
        onBack()
    }



    RightToLeftLayout {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.normal_padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(Dimens.normal_padding),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Icon(painter = painterResource(R.drawable.close),
                        contentDescription = "close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onBack() })

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = "پست جدید",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "بعدی",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.next_color,
                    modifier = Modifier.clickable { next = true}
                )

            }

                selectedImageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "main image",
                        modifier = Modifier.size(screenWidth()),
                        contentScale = ContentScale.Crop
                    )


                }



                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(images) { imageUri ->

                        Image(
                            modifier = Modifier
                                .padding(1.dp)
                                .aspectRatio(1f)
                                .clickable {
                                    selectedImageUri = imageUri
                                },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            painter = rememberAsyncImagePainter(model = imageUri)
                        )
                    }
                }
            }

        if(next){
            AddPostScreenNext(onBack = {next = false}, selectedImageUri = selectedImageUri)
        }

        }
}



fun getGalleryImages(context: Context): List<Uri> {
    val images = mutableListOf<Uri>()

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_ADDED
    )

    // مرتب سازی بر اساس تاریخ ایجاد (نزولی - جدیدترین اول)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            images.add(contentUri)
        }
    }

    return images
}


@Composable
fun HomeScreen(userid : Long, navController: NavHostController) {

    RightToLeftLayout {

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Colors.background))
        {
            AppNameBar()
            DisplayStory()
            MainData(userid, navController)
        }
    }

}

@SuppressLint("ShowToast", "FrequentlyChangedStateReadInComposition")
@Composable
fun MainData(userid : Long, navController: NavHostController) {

    val context = LocalContext.current
    var posts by remember { mutableStateOf<List<PostResponse>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val isAtTop = (listState.firstVisibleItemIndex == 0) &&
            (listState.firstVisibleItemScrollOffset == 0)
    var lastSeenId by remember { mutableStateOf<Long?>(-1) }
    val pageSize = 10
    
    LaunchedEffect(Unit){
        try{
            isLoading = true
            val response = RetrofitInstance.api.getPostsByFollower(userid, lastSeenId, pageSize)
            posts = response
            lastSeenId =response.lastOrNull()?.id
        }
        catch(e : Exception){
            Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
        }
        finally {
            isLoading = false
        }
    }

    LaunchedEffect(listState) {
        //Toast.makeText(context, "state", Toast.LENGTH_SHORT).show()
        snapshotFlow {listState.layoutInfo}
            .collect{ layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalItems = layoutInfo.totalItemsCount

                if (!isLoading && lastVisibleItem != null && lastVisibleItem >= totalItems - 1) {
                    try{
                        isLoading = true
                        val response = RetrofitInstance.api.getPostsByFollower(userid, lastSeenId, pageSize)
                        posts = posts + response
                        lastSeenId =response.lastOrNull()?.id
                    }
                    catch(e : Exception){
                        Toast.makeText(context, e.toString(),Toast.LENGTH_LONG).show()
                    }
                    finally {
                        isLoading = false
                    }
                }
            }
    }

    if(posts.isNotEmpty()) {
        LazyColumn(modifier = Modifier
            .fillMaxSize(),
            state = listState)
        {
            items(posts) { post ->
                PostCard(post, userid, navController)
            }
        }
    }

    if(isLoading){
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center){
            Functions.myCircularProgress()
        }
    }


/*
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                if(isAtTop) {
                    isRefreshing = true
                }

            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize())
            {
                items(posts) { post ->
                    PostCard(post, userid, navController)
                }
            }
        }
        if(isRefreshing){
            LaunchedEffect(Unit) {
                //delay(1500)
                    try{
                        val result = RetrofitInstance.api.getPostsByFollower(userid)
                        posts = result
                        isRefreshing = false
                    }
                    catch(e : Exception){
                        Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
                    }

            }
        }
    }
    */
}


@Composable
fun DisplayStory() {

}

@Composable
fun AppNameBar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(Dimens.normal_padding)
        .background(Colors.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {

           /* IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.heart),
                    contentDescription = "heart",
                    modifier = Modifier.size(25.dp))
            }*/



                Icon(painter = painterResource(R.drawable.chat),
                    contentDescription = "chat",
                    modifier = Modifier
                        .size(30.dp))

            Spacer(modifier = Modifier.size(10.dp))

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(post : PostResponse, userid : Long, navController: NavHostController) {

    var commentSheetState by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    var liked by remember { mutableStateOf(post.isLike) }
    var saved by remember { mutableStateOf(post.isSave) }

   
    val likeScope = rememberCoroutineScope()
    val saveScope = rememberCoroutineScope()
    val context = LocalContext.current
    var likeCount by remember { mutableStateOf(post.likeCount) }
    var commentCount by remember { mutableStateOf(post.commentCount) }


    RightToLeftLayout {

        Column(modifier = Modifier.wrapContentSize()) {

            //profile inf
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(Dimens.normal_padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            )
            {
                AsyncImage(
                    model = post.userProfile,
                    contentDescription = "home_profile_image",
                    modifier = Modifier
                        .size(Dimens.home_profile_image_size)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate("profile/${post.userId}")
                        },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.size(Dimens.normal_spacer))

                Text(
                    text = post.username,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            //Image

            AsyncImage(
                model = post.image,
                contentDescription = "postImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenWidth()),
                contentScale = ContentScale.Crop
            )

            //like/comment/save
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    /*  .background(brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x55000000),
                            Color(0xAA000000),
                            Color(0xDD000000)

                        )))*/
                    .padding(5.dp),
                // .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val scale = remember { Animatable(1f) }


                    LaunchedEffect(liked) {
                        if (liked) {

                            scale.animateTo(1.5f, animationSpec = tween(150))
                            scale.animateTo(
                                1f,
                                animationSpec = tween(300, easing = FastOutSlowInEasing)
                            )
                        } else {

                            scale.snapTo(1f)
                        }
                    }



                    Image(painter = if (liked)
                        painterResource(R.drawable.red_heart)
                    else
                        painterResource(R.drawable.like),
                        contentDescription = "like",
                        modifier = Modifier
                            .size(Dimens.post_icons)
                            .indication(interactionSource = remember { MutableInteractionSource() },
                                indication = null)
                            .graphicsLayer(
                                scaleX = scale.value,
                                scaleY = scale.value
                            )
                            .clickable {
                                liked = !liked
                                likeScope.launch {
                                    if (liked) {
                                        likeCount++
                                        val response = RetrofitInstance.api.likePost(
                                            LikeRequest(
                                                postId = post.id,
                                                userId = userid,
                                                date = "",
                                                time = ""
                                            )
                                        )
                                    } else {
                                        likeCount--
                                        val response = RetrofitInstance.api.disLikePost(
                                            postId = post.id,
                                            userId = userid
                                        )
                                    }
                                }
                            }
                    )


                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = likeCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Image(painter = painterResource(R.drawable.comment),
                        contentDescription = "comment",
                        modifier = Modifier
                            .size(Dimens.post_icons)
                            .clickable { commentSheetState = true }
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = commentCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }


                if (commentSheetState) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { commentSheetState = false },
                        modifier = Modifier.height(300.dp),
                        containerColor = Color.White,
                        scrimColor = Color.Black.copy(alpha = 0.5f)
                    ) {
                        CommentBottomSheet(post, userid)
                    }
                }

                Image(painter = if (saved)
                    painterResource(R.drawable.save_filled)
                else
                    painterResource(R.drawable.save),

                    contentDescription = "save",
                    modifier = Modifier
                        .size(Dimens.post_icons)
                        .clickable {
                            saved = !saved
                            saveScope.launch {
                            if(saved){
                                    val response = RetrofitInstance.api.savePost(
                                        SavePostRequest(
                                            userId = userid,
                                            postId = post.id,
                                            date = "14",
                                            time = "14"
                                        )
                                    )
                                }
                                else{
                                val response = RetrofitInstance.api.unSavePost(
                                        userId = userid,
                                        postId = post.id
                                )
                            }
                            }
                        }

                )

            }


            Spacer(modifier = Modifier.size(Dimens.normal_spacer))

            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(Dimens.normal_padding)
            )


        }
    }
}


@Composable
fun CommentBottomSheet(post : PostResponse, userid : Long){

    val context = LocalContext.current
    var comments by remember { mutableStateOf<List<CommentResponse>>(emptyList()) }
    var newComment by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        try{
            comments = RetrofitInstance.api.getComments(post.id)
            post.commentCount++
        }
        catch (ex : Exception){
            Toast.makeText(context,ex.message, Toast.LENGTH_LONG).show()

        }
    }

    RightToLeftLayout {

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(comments) { comment ->
                    CommentCard(comment)
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Dimens.normal_padding)
                .align(Alignment.BottomEnd)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                }
            ) {
                val userProfile =
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTu8Qi_EGuDWDLusHz6fyxhgaQWa6q0YsOiBH3adnqLx-6_JbLy_-ch2P3xcDxtTh-g9qY&usqp=CAU"

                AsyncImage(
                    model = userProfile,
                    contentDescription = "my profile",
                    modifier = Modifier
                        //.weight(1f)
                        .size(Dimens.comment_user_profile)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)

                )
                
                TextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("نظرت رو بگو") },
                    singleLine = true,
                    modifier = Modifier.weight(5f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Colors.placeholder,
                        unfocusedPlaceholderColor = Colors.placeholder

                    )
                )

                Icon(imageVector = Icons.Filled.Check,
                    contentDescription = "send comment",
                    tint = Colors.appcolor,
                    modifier = Modifier
                        .weight(1f)
                        .size(Dimens.comment_user_profile)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            if(newComment.isNotEmpty()) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val commentRequest = CommentRequest(
                                        postId = post.id,
                                        userId = userid,
                                        comment = newComment,
                                        date = "1404",
                                        time = "18:00"
                                    )
                                    val addedComment =
                                        RetrofitInstance.api.addComment(commentRequest)
                                    comments = comments + addedComment
                                    newComment = ""
                                }
                            }
                        }
                    )

            }
        }
    }

}

@Composable
fun CommentCard(comment: CommentResponse) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(Dimens.normal_padding)
    ) {
        AsyncImage(model = comment.userProfile,
            contentDescription = "user profile",
            modifier = Modifier
                .size(Dimens.comment_user_profile)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
            )

        Spacer(modifier = Modifier.size(10.dp))

        Column(modifier = Modifier
            .wrapContentSize()
        ) {
            Text(text = comment.username,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black)

            Text(text = comment.comment,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black)
        }
    }

}


@Composable
fun LikeHeartWithCustomIcon(
    modifier: Modifier = Modifier,
    isLikedInitial: Boolean = false,
    onLikeChanged: (Boolean) -> Unit = {},
    iconPainter: Painter
) {
    var liked by remember { mutableStateOf(isLikedInitial) }

    val scale by animateFloatAsState(
        targetValue = if (liked) 1.4f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val tint by animateColorAsState(
        targetValue = if (liked) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
            )

            .clickable {
                liked = !liked
                onLikeChanged(liked)
            }
    ) {
        Image(
            painter = iconPainter,
            contentDescription = "Like Heart",
            colorFilter = ColorFilter.tint(tint),
            modifier = Modifier.fillMaxSize()
        )
    }
}


data class NavigationItem(
    val label : String,
    val unselected : Painter,
    val selected : Painter
)



@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    SocialTheme {
        val newComment = CommentResponse(2,
            1,
            "https://bouqs.com/blog/wp-content/uploads/2022/03/shutterstock_260182148-min.jpg",
            "atefeh",
            "new commentrgjrjgkjr rgbrjbgjr  fejbf fbjfbe fjbefbe fkekf e fk efke fekf ke" +
                    "efjebjfbejbfjef je fjbejfbejbfje fje jfbegr kgnktnhkntkhnt kb tkhkb" +
                    "djfjrbgjrbgbrjgbr gjbrjgbkrbg krbkg rkbgkr gkrngk rgrngkr g" +
                    "jrgbrkbgkrgnrngkrnkgnrlgnkrbglrgnkthlthjlyjhfnejfbr gjrbgjbrgrgl" +
                    "gnrgkrgknrkgn",
            "1404/02/10",
            "18:30",
            1
        )

        //CommentBottomSheet(newComment.postId)

        val npost = PostResponse(0,"","","",0,
            "","",0, 0, "",
            false, false)

      //  PostCard(npost, 1)
    }
}