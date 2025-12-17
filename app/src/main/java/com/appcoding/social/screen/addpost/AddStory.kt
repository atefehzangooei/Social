package com.appcoding.social.screen.addpost

import android.content.pm.PackageManager
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.appcoding.social.screen.components.MyFileUtils
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.screen.components.checkReadPermission
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.AddStoryVM
import kotlinx.coroutines.selects.select


@Composable
fun AddStory(navController : NavHostController){
    CameraPermissionWrapper {
        CameraPreview(onSwipUp = {
            AddStoryDisplayImageGallery(navController)
        })
    }
}


@Composable
fun AddStoryDisplayImageGallery(navController: NavHostController) {

    val parentEntry = remember(navController.currentBackStackEntry){
        navController.getBackStackEntry("nav_graph")
    }

    val viewModel : AddStoryVM = hiltViewModel(parentEntry)

    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    val context = LocalContext.current
    val lazyGridState = rememberLazyGridState()
    var images by remember { mutableStateOf(emptyList<Uri>()) }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            images = getGalleryImages(context)
        }
    }

    val permission = checkReadPermission()

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            images = getGalleryImages(context)

        } else {
            permissionLauncher.launch(permission)
        }
    }

    if(images.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = lazyGridState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(images) { imageUri ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(0.5f)
                        .clip(RoundedCornerShape(Dimens.add_story_image_corner))
                        .clickable { viewModel.onSelectedImage(imageUri) },
                    contentDescription = "story images",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

     if(selectedImageUri != null){
         navController.navigate("addimagestory"){
             popUpTo("addstory"){ inclusive = true }
         }
     }

}


@Composable
fun AddImageToStory(navController: NavHostController){

    val context = LocalContext.current
    val parentEntry = remember(navController.currentBackStackEntry){
        navController.getBackStackEntry("nav_graph")
    }
    val viewModel : AddStoryVM = hiltViewModel(parentEntry)
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()
    val state by viewModel.storyState.collectAsState()
    val uploadedStory by viewModel.uploadedStory.collectAsState()
    val myFileUtils = MyFileUtils(context)


    RightToLeftLayout {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(vertical = 15.dp)

        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = "selected story image",
                painter = rememberAsyncImagePainter(model = selectedImageUri)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Black)
                    .padding(15.dp)
                    .align(Alignment.BottomEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(modifier = Modifier
                    .wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.add_story_button_background
                    ),
                    onClick = {
                        selectedImageUri.let { safeUri ->
                            val imageFile = myFileUtils.uriToFile(safeUri!!)
                            Log.d("upload story","isFile = ${imageFile.isFile}")
                            viewModel.uploadStory(imageFile)
                        }
                    }
                ) {
                    AsyncImage(model = profileImage,
                        contentDescription = "profile image",
                        modifier = Modifier
                            .size(Dimens.add_story_profile_image)
                            .shadow(elevation = 2.dp, shape = CircleShape, clip = true)
                            .clip(CircleShape),
                        contentScale = ContentScale.Inside
                        )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(text = "اشتراک گذاری",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium)
                }
            }

            if(state.isUploading){

            }
        }
    }
}