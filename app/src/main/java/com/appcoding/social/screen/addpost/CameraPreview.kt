package com.appcoding.social.screen.addpost

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current
){

    AndroidView(modifier = Modifier.fillMaxSize(),
        factory = {ctx ->

            // 1) ساختن ویو مخصوص CameraX
            val previewView = PreviewView(ctx)

            // 2) گرفتن cameraProvider (غیرهمزمان)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            // 3) وقتی آماده شد، دوربین رو bind کن
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // 4) ساخت Preview
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // 5) انتخاب دوربین پشتی
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // 6) پاک کردن bind قبلی
                    cameraProvider.unbindAll()

                    // 7) بایند کردن دوربین به لایف‌سایکل
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}