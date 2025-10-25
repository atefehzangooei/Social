package com.appcoding.social.screen.addpost

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.appcoding.social.R
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.screen.components.screenWidth
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens


@Composable
fun AddPostScreenNext(onBack: () -> Unit, selectedImageUri: Uri?){

    val context = LocalContext.current
    val imageSize = screenWidth() / 2 +20.dp
    var neveshtak by remember { mutableStateOf("") }

    var isUploading by remember { mutableStateOf(false) }
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
                    /*  if(neveshtak.isNotEmpty() && selectedImageUri != null) {
                          isUploading = true
                          selectedImageUri.let {safeUri ->
                              val imageFile = uriToFile(safeUri, context)
                              uploadPost(neveshtak, imageFile, apiService) { success, message ->
                                  isUploading = false
                                  uploadComplete = true
                                  toastMessage = message
                              }
                          }

                      }*/

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

/*
private fun uploadPost(neveshtak : String, imageFile : File,
                       apiService : ApiService, callback : (Boolean, String) -> Unit){
    val textPart = neveshtak.toRequestBody("text/plain".toMediaTypeOrNull())
    val imagePart = MultipartBody.Part.createFormData(
        "image",
        imageFile.name,
        imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    )

    apiService.addPost(imagePart,textPart).enqueue(object : retrofit2.Callback<String> {
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

*/

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
            if(images.isNotEmpty()) {
                selectedImageUri = images[0]
            }
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
