package com.huhx.picker.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import com.huhx.picker.util.StringUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AssetInfo(
    val id: Long,
    val uriString: String,
    val filename: String,
    val directory: String,
    val size: Long,
    val mediaType: Int,
    val mimeType: String,
    val duration: Long? = null,
    val date: Long,
) {
    fun isImage(): Boolean {
        return mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    }

    fun isGif(): Boolean {
        return mimeType == "image/gif"
    }

    fun isVideo(): Boolean {
        return mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
    }

    fun getBitmap(): Bitmap {
        return BitmapFactory.decodeFile(uriString)
    }

    val resourceType: AssetResourceType = AssetResourceType.fromFileName(uriString)

    // todo: 这种方式还是存在问题
    val randomName: String = run {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val dateTimeString = LocalDateTime.now().format(formatter)
        val fileExtension = filename.split(".")[1]
        val randomString = StringUtil.randomNumeric(6)

        "${dateTimeString}$randomString.$fileExtension"
    }

    fun formatDuration(): String {
        if (duration == null) {
            return ""
        }
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60

        return "${minutes.prefixZero()}:${seconds.prefixZero()}"
    }

    private fun Long.prefixZero(): String {
        return if (this < 10) "0$this" else "$this"
    }
}