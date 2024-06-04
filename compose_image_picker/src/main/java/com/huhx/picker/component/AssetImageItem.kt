package com.huhx.picker.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.huhx.picker.R
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.AssetResourceType

@Composable
fun AssetImageItem(
    modifier: Modifier = Modifier,
    urlString: String,
    onDelete: (() -> Unit)? = null,
    isSelected: Boolean,
    shape: Shape? = null,
    resourceType: AssetResourceType = AssetResourceType.IMAGE,
    durationString: String? = null,
    filterQuality: FilterQuality = FilterQuality.Low,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    navigateToPreview: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    val (backgroundColor, alpha) = if (isSelected) {
        Pair(Color.Black, 0.6F)
    } else {
        Pair(Color.Transparent, 1F)
    }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .alpha(alpha)
            .then(modifier),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(urlString)
                .decoderFactory(VideoFrameDecoder.Factory())
                .crossfade(true)
                .build(),
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1.0F)
                .then(if (shape == null) Modifier else Modifier.clip(shape))
                .combinedClickable(
                    onClick = navigateToPreview,
                    onLongClick = onLongClick,
                ),
            filterQuality = filterQuality,
            onState = onState,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        if (onDelete != null) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(16.dp),
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }

        if (resourceType == AssetResourceType.VIDEO) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 6.dp, end = 6.dp),
                text = durationString ?: "00:00",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        if (resourceType == AssetResourceType.GIF) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 6.dp)
                    .background(
                        color = Color(0F, 0F, 0F, 0.4F),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                    text = stringResource(R.string.text_gif),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun SelectedAssetImageItem(
    assetInfo: AssetInfo,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    resourceType: AssetResourceType = AssetResourceType.IMAGE,
    durationString: String? = null,
    filterQuality: FilterQuality = FilterQuality.None,
    onClick: (AssetInfo) -> Unit,
) {
    val (backgroundColor, alpha) = if (isSelected) {
        Pair(Color.Black, 0.6F)
    } else {
        Pair(Color.Transparent, 1F)
    }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .background(backgroundColor)
            .alpha(alpha),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(assetInfo.uriString)
                .decoderFactory(VideoFrameDecoder.Factory())
                .crossfade(true)
                .build(),
            modifier = modifier
                .aspectRatio(1.0F)
                .then(if (isSelected) Modifier.border(BorderStroke(width = 1.dp, color = Color.Red)) else Modifier)
                .padding(horizontal = 3.dp, vertical = 2.dp)
                .clickable { onClick(assetInfo) },
            filterQuality = filterQuality,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        if (resourceType == AssetResourceType.VIDEO) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 3.dp, end = 3.dp),
                text = durationString ?: "00:00",
                color = Color.White,
                fontSize = 10.sp
            )
        }

        if (resourceType == AssetResourceType.GIF) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 6.dp)
                    .background(
                        color = Color(0F, 0F, 0F, 0.4F),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                    text = stringResource(R.string.text_gif),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}