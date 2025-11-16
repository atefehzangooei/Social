package com.appcoding.social.screen.addpost

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadProgressRequestBody(
    private val file: File,
    private val contentType: String = "image/jpeg",
    private val onProgress: (Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = contentType.toMediaTypeOrNull()

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val total = contentLength()
        var uploaded = 0L

        FileInputStream(file).use { input ->
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                // اگر coroutine/شبکه لغو شود، IOException یا InterruptedException ممکنه بیاد
                sink.write(buffer, 0, read)
                uploaded += read
                val percent = ((uploaded * 100) / total).toInt()
                onProgress(percent.coerceIn(0, 100))
            }
        }
    }
}
