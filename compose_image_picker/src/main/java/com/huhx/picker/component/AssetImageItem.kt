package com.huhx.picker.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
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
import com.huhx.picker.model.AssetResourceType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetImageItem(
    urlString: String,
    isSelected: Boolean,
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
            .alpha(alpha),
        contentAlignment = Alignment.BottomEnd,
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
                .combinedClickable(
                    onClick = navigateToPreview,
                    onLongClick = onLongClick,
                ),
            filterQuality = filterQuality,
            onState = onState,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        if (resourceType == AssetResourceType.VIDEO) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
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