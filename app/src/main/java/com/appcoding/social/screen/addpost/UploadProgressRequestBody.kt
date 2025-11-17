package com.appcoding.social.screen.addpost

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadProgressRequestBody(
    private val file: File,
    private val contentType: MediaType?,
    private val onProgress: (Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = contentLength()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val input = file.inputStream()
        var uploaded = 0L
        var read: Int

        input.use {
            while (input.read(buffer).also { read = it } != -1) {
                uploaded += read
                sink.write(buffer, 0, read)
                val progress = (uploaded * 100 / length).toInt()
                onProgress(progress)
            }
        }
    }
}
