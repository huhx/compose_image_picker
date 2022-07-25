package com.huhx.picker.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetPickerRepository
import com.huhx.picker.data.AssetPickerRoute
import com.huhx.picker.data.AssetViewModel
import com.huhx.picker.data.AssetViewModelFactory
import com.huhx.picker.data.currentRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QQAssetPicker(
    assetPickerConfig: AssetPickerConfig,
    onPicked: (List<AssetInfo>) -> Unit,
) {
    val context = LocalContext.current
    val navController = rememberAnimatedNavController()
    val viewModel: AssetViewModel = viewModel(
        factory = AssetViewModelFactory(
            assetPickerRepository = AssetPickerRepository(context),
            assetPickerConfig = assetPickerConfig,
            navController = navController
        )
    )

    LaunchedEffect(Unit, block = {
        viewModel.initAssets()
    })

    val isHome = currentRoute(navController) == "home"

    Scaffold(
        topBar = { AssetTopBar(viewModel, isHome, onPicked, navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AssetPickerRoute(
                navController = navController,
                viewModel = viewModel,
            ) {
                navController.navigateUp()
                viewModel.expanded.value = !viewModel.expanded.value
                if (viewModel.folderName.value != it) {
                    viewModel.folderName.value = it
                }
            }
        }
    }
}

@Composable
private fun AssetTopBar(
    viewModel: AssetViewModel,
    isHome: Boolean,
    onPicked: (List<AssetInfo>) -> Unit,
    navController: NavHostController
) {
    val isPreview = currentRoute(navController)?.startsWith("preview")
    if (isPreview != true) {
        TopAppBar(
            viewModel = viewModel,
            navigateUp = {
                if (isHome) {
                    onPicked(viewModel.selectedList)
                } else {
                    navController.navigateUp()
                }
            },
            navigateToDropDown = {
                navController.navigate("dropDown?directory=$it")
            },
            onPicked = { onPicked(it) }
        )
    }
}


@Composable
fun TopAppBar(
    viewModel: AssetViewModel,
    navigateUp: () -> Unit,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    val folderName = viewModel.folderName.value
    val expanded = viewModel.expanded

    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    Icons.Filled.Close,
                    tint = Color.Black,
                    contentDescription = "",
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.clickable {
                    expanded.value = !expanded.value
                    if (expanded.value) navigateToDropDown(folderName) else navigateUp()
                }
            ) {
                Text(folderName, fontSize = 18.sp)
                if (expanded.value) {
                    Icon(Icons.Default.KeyboardArrowUp, "")
                } else {
                    Icon(Icons.Default.KeyboardArrowDown, "")
                }
            }
        },
        actions = {
            Button(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                enabled = viewModel.isEnable,
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = { onPicked(viewModel.selectedList) }
            ) {
                Text(viewModel.selectedText)
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabView(
    viewModel: AssetViewModel,
) {
    val tabs = listOf(TabItem.All, TabItem.Video, TabItem.Image)
    val pagerState = rememberPagerState()
    Column {
        Tab(tabs = tabs, pagerState = pagerState)
        TabsContent(
            tabs = tabs,
            pagerState = pagerState,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Tab(
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
        userScrollEnabled = false
    ) { page ->
        tabs[page].screen(viewModel)
    }
}
