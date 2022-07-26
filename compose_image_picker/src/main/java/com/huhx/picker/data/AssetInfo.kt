package com.huhx.picker.data

import android.net.Uri
import android.provider.MediaStore
import com.huhx.picker.constant.prefixZero

data class AssetInfo(
    val id: Long,
    val uri: Uri,
    val filename: String,
    val directory: String,
    val mediaType: String,
    val mimeType: String,
    val duration: Long?,
    val date: Long,
) {
    fun isImage(): Boolean {
        return mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
    }

    fun isVideo(): Boolean {
        return mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
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