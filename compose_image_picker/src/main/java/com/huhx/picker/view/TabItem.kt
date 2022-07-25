package com.huhx.picker.view

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.huhx.picker.R
import com.huhx.picker.data.AssetViewModel

sealed class TabItem(
    @StringRes val resourceId: Int,
    val screen: @Composable (AssetViewModel) -> Unit
) {
    object All : TabItem(R.string.tab_item_all, { viewModel ->
        AssetAll(viewModel)
    })

    object Video : TabItem(R.string.tab_item_video, { viewModel ->
        AssetVideo(viewModel)
    })

    object Image : TabItem(R.string.tab_item_image, { viewModel ->
        AssetImage(viewModel)
    })
}