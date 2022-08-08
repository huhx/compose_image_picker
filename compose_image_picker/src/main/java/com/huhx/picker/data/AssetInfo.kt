package com.huhx.picker.data

import android.provider.MediaStore
import com.huhx.picker.constant.prefixZero

data class AssetInfo(
    val id: Long,
    val uriString: String,
    val filename: String,
    val directory: String,
    val size: Long,
    val mediaType: Int,
    val mimeType: String,
    val duration: Long?,
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

    fun formatDuration(): String {
        if (duration == null) {
            return ""
        }
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60

        return "${minutes.prefixZero()}:${seconds.prefixZero()}"
    }
}