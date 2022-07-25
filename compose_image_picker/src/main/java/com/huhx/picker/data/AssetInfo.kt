package com.huhx.picker.data

import android.net.Uri

data class AssetInfo(
    val id: Long,
    val uri: Uri,
    val filename: String,
    val directory: String,
    val mediaType: String,
    val mimeType: String,
    val duration: Long?,
    val date: Long,
)