package com.huhx.picker.view

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.huhx.picker.R
import com.huhx.picker.component.SelectedAssetImageItem
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.util.vibration
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun AssetPreviewScreen(
    index: Int,
    assets: List<AssetInfo>,
    navigateUp: () -> Unit,
    selectedList: SnapshotStateList<AssetInfo>,
    onSelectChanged: (AssetInfo) -> Unit,
) {
    val pageState = key(assets) {
        rememberPagerState(initialPage = index, pageCount = assets::size)
    }

    val scope = rememberCoroutineScope()
    val assetInfo = assets[pageState.currentPage]

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (assetInfo.isImage()) {
                                stringResource(id = R.string.preview_title_image)
                            } else {
                                stringResource(id = R.string.preview_title_video)
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.White)
                        )
                        Text(
                            text = assetInfo.dateString,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = Color.White, contentDescription = "")
                    }
                },
                actions = {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "${pageState.currentPage + 1}/${assets.size}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.White)
                    )
                },
            )
        },
        bottomBar = {
            SelectorBottomBar(selectedList = selectedList, assetInfo = assetInfo) {
                navigateUp()
                if (selectedList.isEmpty()) selectedList.add(it)
            }
        }
    ) {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .padding(it)
                .background(Color.Black),
            contentAlignment = Alignment.BottomCenter
        ) {
            AssetPreview(assets = assets, pagerState = pageState)

            if (selectedList.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.8F))
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    itemsIndexed(selectedList) { _, resource ->
                        SelectedAssetImageItem(
                            assetInfo = resource,
                            isSelected = resource.id == assetInfo.id,
                            resourceType = resource.resourceType,
                            durationString = resource.formatDuration(),
                            modifier = Modifier.size(64.dp),
                            onClick = { asset ->
                                val selectedIndex = assets.indexOfFirst { item -> item.id == asset.id }
                                // 如果index == -1，说明该asset 是存在于别的时间
                                if (selectedIndex == -1) {
                                    onSelectChanged(asset)
                                    context.vibration(50L)
                                    return@SelectedAssetImageItem
                                }

                                scope.launch {
                                    pageState.animateScrollToPage(selectedIndex)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@UnstableApi
@Composable
fun SelectorBottomBar(
    assetInfo: AssetInfo,
    selectedList: SnapshotStateList<AssetInfo>,
    onClick: (AssetInfo) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha = 0.9F))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AssetImageIndicator(
                assetInfo = assetInfo,
                selected = selectedList.any { it == assetInfo },
                assetSelected = selectedList,
                fontSize = 14.sp,
                size = 22.dp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = stringResource(R.string.text_asset_select), color = Color.White, fontSize = 14.sp)
        }
        Button(
            modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            onClick = { onClick(assetInfo) }
        ) {
            Text(text = stringResource(R.string.text_done), color = Color.White, fontSize = 15.sp)
        }
    }
}

@UnstableApi
@Composable
private fun AssetPreview(assets: List<AssetInfo>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 0.dp),
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val assetInfo = assets[page]
        if (assetInfo.isImage()) {
            ImagePreview(uriString = assetInfo.uriString)
        } else {
            VideoPreview(uriString = assetInfo.uriString)
        }
    }
}

@Composable
fun ImagePreview(
    modifier: Modifier = Modifier,
    uriString: String,
    loading: (@Composable () -> Unit)? = null,
) {
    val painter = rememberAsyncImagePainter(
        filterQuality = FilterQuality.Low,
        model = ImageRequest.Builder(LocalContext.current)
            .data(uriString)
            .decoderFactory(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
            .build()
    )

    if (loading != null && painter.state is AsyncImagePainter.State.Loading) {
        loading()
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    )
}

@UnstableApi
@Composable
fun VideoPreview(
    modifier: Modifier = Modifier,
    uriString: String,
    loading: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
            val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uriString))
            setMediaSource(source)

            prepare()
        }
    }

    if (loading != null && player.isLoading) {
        loading()
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        factory = {
            PlayerView(it).apply {
                this.player = player
                setShowPreviousButton(false)
                setShowNextButton(false)
                setShowFastForwardButton(false)
                setShowRewindButton(false)
                setShowSubtitleButton(false)
            }
        })
}
