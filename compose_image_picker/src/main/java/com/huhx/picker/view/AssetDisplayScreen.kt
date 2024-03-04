package com.huhx.picker.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx.picker.R
import com.huhx.picker.component.AssetImageItem
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.RequestType
import com.huhx.picker.viewmodel.AssetViewModel
import kotlinx.coroutines.launch

@Composable
internal fun AssetDisplayScreen(
    viewModel: AssetViewModel,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    BackHandler {
        if (viewModel.selectedList.isNotEmpty()) {
            viewModel.clear()
        } else {
            onClose(viewModel.selectedList)
        }
    }

    Scaffold(
        topBar = {
            DisplayTopAppBar(
                directory = viewModel.directory,
                selectedList = viewModel.selectedList,
                navigateUp = onClose,
                navigateToDropDown = navigateToDropDown
            )
        },
        bottomBar = {
            DisplayBottomBar(viewModel, onPicked)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            val tabs = listOf(TabItem.All, TabItem.Video, TabItem.Image)
            val pagerState = rememberPagerState(pageCount = tabs::size)

            Column {
                AssetTab(tabs = tabs, pagerState = pagerState)
                HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
                    tabs[page].screen(viewModel)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DisplayTopAppBar(
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
private fun DisplayBottomBar(viewModel: AssetViewModel, onPicked: (List<AssetInfo>) -> Unit) {
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
                content = { Text(text = stringResource(R.string.label_camera), fontSize = 16.sp, color = Color.Gray) }
            )
            TextButton(
                onClick = {},
                content = { Text(text = stringResource(R.string.label_album), fontSize = 16.sp) }
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

@Composable
private fun AssetTab(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    TabRow(selectedTabIndex = pagerState.currentPage, indicator = {}) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = pagerState.currentPage == index,
                text = { Text(text = stringResource(tab.resourceId)) },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = Color.Gray,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AssetContent(viewModel: AssetViewModel, requestType: RequestType) {
    val assets = viewModel.getGroupedAssets(requestType)
    val context = LocalContext.current
    val gridCount = LocalAssetConfig.current.gridCount
    val maxAssets = LocalAssetConfig.current.maxAssets
    val errorMessage = stringResource(R.string.message_selected_exceed, maxAssets)

    if (assets.isEmpty()) {
        return Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "对应的资源为空",
                textAlign = TextAlign.Center
            )
        }
    }

    LazyColumn {
        assets.forEach { (dateString, resources) ->
            val allSelected = viewModel.isAllSelected(resources)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    TextButton(onClick = {
                        if (allSelected) {
                            viewModel.unSelectAll(resources)
                        } else {
                            if (viewModel.selectAll(resources, maxAssets)) {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Text(
                            text = if (allSelected) {
                                stringResource(id = R.string.text_deselect_all)
                            } else {
                                stringResource(id = R.string.text_select_all)
                            }
                        )
                    }
                }
            }

            item {
                val itemSize: Dp = (LocalConfiguration.current.screenWidthDp.dp / gridCount)
                FlowRow(maxItemsInEachRow = gridCount) {
                    resources.forEachIndexed { index, assetInfo ->
                        AssetImage(
                            modifier = Modifier
                                .size(itemSize)
                                .padding(horizontal = 1.dp, vertical = 1.dp),
                            assetInfo = assetInfo,
                            navigateToPreview = { viewModel.navigateToPreview(index, dateString, requestType) },
                            selectedList = viewModel.selectedList,
                            onLongClick = { selected -> viewModel.toggleSelect(selected, assetInfo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetImage(
    modifier: Modifier = Modifier,
    assetInfo: AssetInfo,
    selectedList: SnapshotStateList<AssetInfo>,
    navigateToPreview: () -> Unit,
    onLongClick: (Boolean) -> Unit,
) {
    val selected = selectedList.any { it.id == assetInfo.id }
    val context = LocalContext.current
    val maxAssets = LocalAssetConfig.current.maxAssets
    val errorMessage = stringResource(R.string.message_selected_exceed, maxAssets)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd,
    ) {
        AssetImageItem(
            filterQuality = FilterQuality.None,
            urlString = assetInfo.uriString,
            isSelected = selected,
            resourceType = assetInfo.resourceType,
            durationString = assetInfo.formatDuration(),
            navigateToPreview = navigateToPreview,
            onLongClick = {
                val selectResult = !selected
                if (!selectResult || selectedList.size < maxAssets) {
                    onLongClick(selectResult)
                } else {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
        AssetImageIndicator(assetInfo = assetInfo, selected = selected, assetSelected = selectedList)
    }
}

private sealed class TabItem(
    @StringRes val resourceId: Int,
    val screen: @Composable (AssetViewModel) -> Unit,
) {
    data object All : TabItem(R.string.tab_item_all, { viewModel -> AssetContent(viewModel, RequestType.COMMON) })

    data object Video : TabItem(R.string.tab_item_video, { viewModel -> AssetContent(viewModel, RequestType.VIDEO) })

    data object Image : TabItem(R.string.tab_item_image, { viewModel -> AssetContent(viewModel, RequestType.IMAGE) })
}
