package com.huhx.picker.constant

data class AssetPickerConfig(
    val maxAssets: Int = 9,
    val gridCount: Int = 3,
    val requestType: RequestType = RequestType.COMMON,
)