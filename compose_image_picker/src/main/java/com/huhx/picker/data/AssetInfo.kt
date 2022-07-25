package com.huhx.picker.data

import android.net.Uri
import android.provider.MediaStore

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
    fun isVideo(): Boolean {
        return mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    }

    fun formatDuration(): String {
        if (duration == null) {
            return ""
        }
        val minutes = duration / 1000 / 60
        val minutesString = if (minutes < 9) {
            "0$minutes"
        } else {
            "$minutes"
        }
        val seconds = duration / 1000 % 60
        val secondsString = if (seconds < 9) {
            "0$seconds"
        } else {
            "$seconds"
        }
        return "$minutesString:$secondsString"
    }
}

fun main() {
    println(17153 / 1000)
}