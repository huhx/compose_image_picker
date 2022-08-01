package com.huhx.picker.view

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.R
import com.huhx.picker.constant.RequestType
import com.huhx.picker.constant.showShortToast
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    viewModel: AssetViewModel,
    navController: NavHostController,
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    Scaffold(
        topBar = {
            val directory = viewModel.directory
            HomeTopAppBar(
                directory = directory,
                selectedList = viewModel.selectedList,
                navigateUp = onClose,
                onPicked = onPicked,
                navigateToDropDown = { navController.navigate("dropDown?directory=$directory") }
            )
        }
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
fun HomeTopAppBar(
    directory: String,
    selectedList: List<AssetInfo>,
    navigateUp: (List<AssetInfo>) -> Unit,
    navigateToDropDown: () -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = { navigateUp(selectedList) }) {
                Icon(
                    Icons.Filled.Close,
                    tint = Color.Black,
                    contentDescription = "",
                )
            }
        },
        title = {
            Row(modifier = Modifier.clickable { navigateToDropDown() }) {
                Text(directory, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowDown, "")
            }
        },
        actions = {
            AppBarButton(
                size = selectedList.size,
                onPicked = { onPicked(selectedList) }
            )
        }
    )
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
        contentColor = Color.Black,
        indicator = {},
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = pagerState.currentPage == index,
                text = { Text(text = stringResource(tab.resourceId)) },
                selectedContentColor = Color.Black,
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
        item { AssetCamera() }

        itemsIndexed(assets, key = { _, it -> it.id }) { index, it ->
            AssetImage(
                assetInfo = it,
                index = index,
                requestType = requestType,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun AssetImage(
    assetInfo: AssetInfo,
    index: Int,
    requestType: RequestType,
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
                    .data(assetInfo.uriString)
                    .decoderFactory(VideoFrameDecoder.Factory())
                    .build(),
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.0F)
                    .clickable { viewModel.navigateToPreview(index, requestType) },
                filterQuality = FilterQuality.None,
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
                        text = "GIF",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }

        AssetImageIndicator(
            assetInfo = assetInfo,
            selected = selected,
            assetSelected = viewModel.selectedList
        ) { isSelected ->
            if (viewModel.isFullSelected() && isSelected) {
                context.showShortToast("已经达到最大值${viewModel.assetPickerConfig.maxAssets}了")
                return@AssetImageIndicator
            }
            if (isSelected) {
                viewModel.selectedList.add(assetInfo)
            } else {
                viewModel.selectedList.remove(assetInfo)
            }
        }
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
