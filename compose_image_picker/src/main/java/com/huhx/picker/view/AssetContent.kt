package com.huhx.picker.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.huhx.picker.R
import com.huhx.picker.constant.showShortToast
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel

@Composable
fun AssetAll(
    viewModel: AssetViewModel
) {
    QQAssetContent(viewModel)
}

@Composable
fun AssetVideo(
    viewModel: AssetViewModel
) {
    QQAssetContent(viewModel)
}

@Composable
fun AssetImage(
    viewModel: AssetViewModel
) {
    QQAssetContent(viewModel)
}

@Composable
fun QQAssetContent(
    viewModel: AssetViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(viewModel.assetPickerConfig.gridCount),
        contentPadding = PaddingValues(horizontal = 1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        userScrollEnabled = true
    ) {
        item {
            AssetCamera()
        }

        items(viewModel.assets) {
            AssetImage(assetInfo = it, viewModel = viewModel)
        }
    }
}

@Composable
fun AssetCamera() {
    val context = LocalContext.current
    Image(
        modifier = Modifier
            .fillMaxSize()
            .clickable { context.showShortToast("open the camera") },
        contentScale = ContentScale.Crop,
        painter = painterResource(id = R.drawable.app_icon_foreground), contentDescription = ""
    )
}

@Composable
fun AssetImage(
    assetInfo: AssetInfo,
    viewModel: AssetViewModel
) {
    val selected = viewModel.isSelected(assetInfo)

    val (backgroundColor, alpha) = if (selected) {
        Pair(Color.Black, 0.6F)
    } else {
        Pair(Color.Transparent, 1F)
    }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .alpha(alpha),
            contentAlignment = Alignment.BottomEnd,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(assetInfo.uri)
                    .decoderFactory(VideoFrameDecoder.Factory())
                    .build(),
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.0F)
                    .clickable { viewModel.navigateToPreview(assetInfo) },
                filterQuality = FilterQuality.None,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            if (assetInfo.isVideo()) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp, end = 10.dp),
                    text = assetInfo.formatDuration(),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        AssetImageIndicator(assetInfo, selected, viewModel.selectedList) { isSelected ->
            if (viewModel.isFullSelected() && isSelected) {
                context.showShortToast("已经达到最大值${viewModel.assetPickerConfig.maxAssets}了")
                return@AssetImageIndicator
            }
            if (isSelected) {
                viewModel.add(assetInfo)
            } else {
                viewModel.remove(assetInfo)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetImageIndicator(
    assetInfo: AssetInfo,
    selected: Boolean,
    assetSelected: SnapshotStateList<AssetInfo>,
    onClick: (Boolean) -> Unit
) {
    Surface(
        onClick = { onClick(!selected) },
        modifier = Modifier
            .padding(6.dp)
            .size(size = 24.dp)
            .clickable { onClick(false) },
        shape = CircleShape,
        border = if (!selected) BorderStroke(width = 1.dp, color = Color.White) else null,
        color = if (selected) Color(64, 151, 246) else Color(0f, 0f, 0f, 0.3F)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (selected) {
                val num = assetSelected.indexOf(assetInfo) + 1
                Text(
                    text = "${if (selected) num else null}",
                    color = Color.White
                )
            }
        }
    }
}
