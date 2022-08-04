package com.huhx.picker.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.R
import com.huhx.picker.constant.RequestType
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun AssetDisplayScreen(
    viewModel: AssetViewModel,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    Scaffold(
        topBar = {
            val directory = viewModel.directory
            DisplayTopAppBar(
                directory = directory,
                selectedList = viewModel.selectedList,
                navigateUp = onClose,
                navigateToDropDown = navigateToDropDown
            )
        },
        bottomBar = { DisplayBottomBar(viewModel, onPicked) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            val tabs = listOf(TabItem.All, TabItem.Video, TabItem.Image)
            val pagerState = rememberPagerState()

            Column {
                AssetTab(tabs = tabs, pagerState = pagerState)
                TabsContent(
                    tabs = tabs,
                    pagerState = pagerState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTopAppBar(
    directory: String,
    selectedList: List<AssetInfo>,
    navigateUp: (List<AssetInfo>) -> Unit,
    navigateToDropDown: (String) -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = { navigateUp(selectedList) }) {
                Icon(Icons.Filled.Close, contentDescription = "")
            }
        },
        title = {
            Row(modifier = Modifier.clickable { navigateToDropDown(directory) }) {
                Text(directory, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
            }
        },
    )
}

@Composable
private fun DisplayBottomBar(
    viewModel: AssetViewModel,
    onPicked: (List<AssetInfo>) -> Unit
) {
    var cameraUri: Uri? by remember { mutableStateOf(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraUri?.let { viewModel.initDirectories() }
        } else {
            viewModel.deleteImage(cameraUri)
        }
    }

    if (viewModel.selectedList.isEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(
                onClick = {
                    cameraUri = viewModel.getUri()
                    cameraLauncher.launch(cameraUri)
                },
                content = {
                    Text(text = stringResource(R.string.label_camera), fontSize = 16.sp, color = Color.Gray)
                }
            )
            TextButton(
                onClick = {},
                content = {
                    Text(text = stringResource(R.string.label_album), fontSize = 16.sp)
                }
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.text_select_tip), fontSize = 12.sp, color = Color.Gray)
            AppBarButton(
                size = viewModel.selectedList.size,
                onPicked = { onPicked(viewModel.selectedList) }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun AssetTab(
    tabs: List<TabItem>,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = {},
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = pagerState.currentPage == index,
                text = { Text(text = stringResource(tab.resourceId)) },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = Color.Gray,
                onClick = {
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(
    tabs: List<TabItem>,
    pagerState: PagerState,
    viewModel: AssetViewModel
) {
    HorizontalPager(
        state = pagerState,
        count = tabs.size,
        userScrollEnabled = true
    ) { page ->
        tabs[page].screen(viewModel)
    }
}

@Composable
fun QQAssetContent(
    viewModel: AssetViewModel,
    requestType: RequestType
) {
    val assets = viewModel.getAssets(requestType)
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(viewModel.assetPickerConfig.gridCount),
        contentPadding = PaddingValues(horizontal = 1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        userScrollEnabled = true
    ) {
        itemsIndexed(assets, key = { _, it -> it.id }) { index, it ->
            AssetImage(
                assetInfo = it,
                index = index,
                requestType = requestType,
                viewModel = viewModel,
                selectedList = viewModel.selectedList
            )
        }
    }
}

@Composable
fun AssetImage(
    assetInfo: AssetInfo,
    selectedList: SnapshotStateList<AssetInfo>,
    index: Int,
    requestType: RequestType,
    viewModel: AssetViewModel
) {
    val selected = selectedList.any { it.id == assetInfo.id }

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
                    .data(assetInfo.uriString)
                    .decoderFactory(VideoFrameDecoder.Factory())
                    .build(),
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.0F)
                    .clickable { viewModel.navigateToPreview(index, requestType) },
                filterQuality = FilterQuality.Low,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            if (assetInfo.isVideo()) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp, end = 8.dp),
                    text = assetInfo.formatDuration(),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            if (assetInfo.isGif()) {
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
        AssetImageIndicator(assetInfo = assetInfo, selected = selected, assetSelected = selectedList)
    }
}

sealed class TabItem(
    @StringRes val resourceId: Int,
    val screen: @Composable (AssetViewModel) -> Unit
) {
    object All : TabItem(R.string.tab_item_all, { viewModel ->
        QQAssetContent(viewModel, RequestType.COMMON)
    })

    object Video : TabItem(R.string.tab_item_video, { viewModel ->
        QQAssetContent(viewModel, RequestType.VIDEO)
    })

    object Image : TabItem(R.string.tab_item_image, { viewModel ->
        QQAssetContent(viewModel, RequestType.IMAGE)
    })
}
