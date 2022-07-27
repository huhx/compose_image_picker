package com.huhx.picker.view

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

@Composable
fun AssetPreview(
    uri: Uri,
    isVideo: Boolean,
    navigateBack: () -> Unit
) {
    Surface(
        color = Color.Black,
        modifier = Modifier
            .fillMaxSize()
            .clickable { navigateBack() }
    ) {
        if (isVideo) {
            VideoPlayer(uri)
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .decoderFactory(if (SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
                    .build(),
                modifier = Modifier.fillMaxSize(),
                filterQuality = FilterQuality.None,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            setMediaSource(source)
            playWhenReady = true
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
