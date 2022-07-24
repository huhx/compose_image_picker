package com.huhx.picker.view

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.huhx.picker.R
import com.huhx.picker.constant.AssetPickerConfig

sealed class TabItem(
    @StringRes val resourceId: Int,
    val screen: @Composable (AssetPickerConfig, SnapshotStateList<String>) -> Unit
) {
    object All : TabItem(R.string.tab_item_all, { config, assetSelected ->
        AllScreen(config, assetSelected)
    })

    object Video : TabItem(R.string.tab_item_video, { config, assetSelected ->
        VideoScreen(config, assetSelected)
    })

    object Image : TabItem(R.string.tab_item_image, { config, assetSelected ->
        ImageScreen(config, assetSelected)
    })
}