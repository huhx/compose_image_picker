package com.huhx.picker.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.huhx.picker.constant.AssetPickerConfig

@Composable
fun AllScreen(
    config: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    QQAssetContent(config, assetSelected)
}

@Composable
fun VideoScreen(
    config: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    QQAssetContent(config, assetSelected)
}

@Composable
fun ImageScreen(
    config: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    QQAssetContent(config, assetSelected)
}