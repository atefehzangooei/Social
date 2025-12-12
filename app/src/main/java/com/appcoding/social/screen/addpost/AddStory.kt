package com.appcoding.social.screen.addpost

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.ActivityNavigatorExtras
import coil.compose.rememberAsyncImagePainter
import com.appcoding.social.screen.components.checkReadPermission
import com.appcoding.social.ui.theme.Dimens
import kotlinx.coroutines.selects.select


@Composable
fun AddStory(){
    CameraPermissionWrapper {
        CameraPreview(onSwipUp = {
            val selectedImageUri = DisplayGalleyImages()
            if(selectedImageUri != null){

            }
        })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayGalleyImages() : Uri?{

    val context = LocalContext.current
    var images by remember { mutableStateOf(emptyList<Uri>()) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lazyGridState = rememberLazyGridState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


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

    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = Color.Black,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        onDismissRequest = {}
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = lazyGridState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(images){ imageUri ->
                Image(painter = rememberAsyncImagePainter(model = imageUri),
                    modifier = Modifier
                        .padding(1.dp)
                        .aspectRatio(0.5f)
                        .clip(RoundedCornerShape(Dimens.add_story_image_corner))
                        .clickable { selectedImageUri = imageUri },
                    contentDescription = "story images",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    return selectedImageUri

}