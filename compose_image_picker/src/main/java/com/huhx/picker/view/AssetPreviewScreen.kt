package com.huhx.picker.view

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.huhx.picker.R
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun AssetPreviewScreen(
    index: Int,
    assets: List<AssetInfo>,
    navController: NavHostController,
    viewModel: AssetViewModel,
) {
    val pageState = rememberPagerState(initialPage = index)

    Scaffold(
        topBar = { PreviewTopAppBar(navigateUp = { navController.navigateUp() }) },
        bottomBar = {
            SelectorBottomBar(
                selectedList = viewModel.selectedList,
                assetInfo = assets[pageState.currentPage]
            ) {
                navController.navigateUp()
                if (viewModel.selectedList.isEmpty()) {
                    viewModel.selectedList.add(it)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            AssetPreview(assets = assets, pagerState = pageState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewTopAppBar(navigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        ),
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "",
                )
            }
        }
    )
}

@Composable
private fun SelectorBottomBar(
    assetInfo: AssetInfo,
    selectedList: SnapshotStateList<AssetInfo>,
    onClick: (AssetInfo) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha = 0.9F))
            .padding(horizontal = 10.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AssetImageIndicator(
                assetInfo = assetInfo,
                size = 20.dp,
                fontSize = 14.sp,
                selected = selectedList.any { it == assetInfo },
                assetSelected = selectedList,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(R.string.text_asset_select), color = Color.White, fontSize = 14.sp)
        }
        Button(
            modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
            shape = RoundedCornerShape(5.dp),
            enabled = true,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            onClick = { onClick(assetInfo) }
        ) {
            Text(stringResource(R.string.text_done), color = Color.White, fontSize = 15.sp)
        }
    }
}

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
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uriString))
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
