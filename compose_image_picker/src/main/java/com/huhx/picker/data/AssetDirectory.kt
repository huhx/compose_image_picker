package com.huhx.picker.data

import android.net.Uri

data class AssetDirectory(
    val directory: String,
    val assets: List<AssetInfo>,
    val cover: Uri = assets.first().uri,
    val counts: Int = assets.size
)