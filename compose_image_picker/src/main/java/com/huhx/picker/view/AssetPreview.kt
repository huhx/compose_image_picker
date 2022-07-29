package com.huhx.picker.view

import android.os.Build
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.huhx.picker.data.AssetInfo
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AssetPreview(
    assets: List<AssetInfo>,
    pagerState: PagerState
) {
    Box {
        HorizontalPager(
            count = assets.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ImageItem(assets[page], this)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageItem(
    assetInfo: AssetInfo,
    pagerScope: PagerScope
) {
    var scale by remember { mutableStateOf(1f) }
    val xOffset by remember { mutableStateOf(0f) }
    val yOffset by remember { mutableStateOf(0f) }

    val state = rememberTransformableState(onTransformation = { zoomChange, _, _ ->
        val tempScale = (zoomChange * scale).coerceAtLeast(1f)
        scale = (if (tempScale > 5f) 5f else tempScale)
    })

    if (assetInfo.isVideo()) {
        VideoPlayer(uriString = assetInfo.uriString)
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(assetInfo.uriString)
                .decoderFactory(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
                .build(),
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = state)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = xOffset
                    translationY = yOffset
                    val pageOffset = pagerScope.currentPageOffset.absoluteValue
                    if (pageOffset == 1.0f) {
                        scale = 1.0f
                    }
                },
            filterQuality = FilterQuality.None,
            contentScale = ContentScale.Fit,
            contentDescription = ""
        )
    }
}

@Composable
fun VideoPlayer(uriString: String) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
            val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uriString))
            setMediaSource(source)

            prepare()
        }
    }

    DisposableEffect(
        AndroidView(
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    ) {
        onDispose { exoPlayer.release() }
    }
}
