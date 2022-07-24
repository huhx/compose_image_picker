package com.huhx.picker.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.R
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.constant.showShortToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QQAssetPicker(
    assetPickerConfig: AssetPickerConfig,
    onPicked: (List<String>) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val folderName = remember { mutableStateOf("所有项目") }
    val navController = rememberAnimatedNavController()
    val isHome = currentRoute(navController) == "home"
    val assetSelected = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                expanded = expanded,
                folderName = folderName,
                assetPickerConfig = assetPickerConfig,
                assetSelected = assetSelected,
                navigateUp = {
                    if (isHome) {
                        onPicked(assetSelected)
                    } else {
                        navController.navigateUp()
                    }
                },
                navigateToDropDown = {
                    navController.navigate("dropDown?directory=$it")
                }
            ) { onPicked(it) }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AssetPickerRoute(
                navController = navController,
                assetPickerConfig = assetPickerConfig,
                assetSelected = assetSelected,
            ) {
                navController.navigateUp()
                expanded.value = !expanded.value
                if (folderName.value != it) {
                    folderName.value = it
                }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AssetPickerRoute(
    navController: NavHostController,
    assetPickerConfig: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>,
    navigateBack: (String) -> Unit,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            TabView(assetPickerConfig, assetSelected)
        }

        composable(
            "dropDown?directory={directory}",
            arguments = listOf(navArgument("directory") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            val directory = arguments.getString("directory")!!
            FolderDropDown(directory) { navigateBack(it) }
        }
    }
}

@Composable
fun TopAppBar(
    expanded: MutableState<Boolean>,
    folderName: MutableState<String>,
    assetPickerConfig: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>,
    navigateUp: () -> Unit,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<String>) -> Unit
) {
    val isEnable = assetSelected.size > 0
    val text = if (isEnable) "确定(${assetSelected.size}/${assetPickerConfig.maxAssets})" else "确定"

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
                    if (expanded.value) {
                        navigateToDropDown(folderName.value)
                    } else {
                        navigateUp()
                    }
                }
            ) {
                Text(folderName.value, fontSize = 18.sp)
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
                enabled = isEnable,
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = { onPicked(assetSelected) }
            ) {
                Text(text)
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabView(
    assetPickerConfig: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    val tabs = listOf(
        TabItem.All,
        TabItem.Video,
        TabItem.Image,
    )
    val pagerState = rememberPagerState()
    Column {
        Tab(tabs = tabs, pagerState = pagerState)
        TabsContent(
            tabs = tabs,
            pagerState = pagerState,
            config = assetPickerConfig,
            assetSelected = assetSelected
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
    config: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen(config, assetSelected)
    }
}

@Composable
fun QQAssetContent(
    config: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(config.gridCount),
        contentPadding = PaddingValues(horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        userScrollEnabled = true
    ) {
        item {
            AssetCamera()
        }
        items(100) { index ->
            AssetImage(index, config, assetSelected)
        }
    }
}

@Composable
fun AssetCamera() {
    val context = LocalContext.current
    Image(
        modifier = Modifier
            .fillMaxSize()
            .clickable { context.showShortToast("open the camera") },
        contentScale = ContentScale.Crop,
        painter = painterResource(id = R.drawable.app_icon_foreground), contentDescription = ""
    )
}

@Composable
fun AssetImage(
    index: Int,
    config: AssetPickerConfig,
    assetSelected: SnapshotStateList<String>
) {
    val selected = remember { mutableStateOf(assetSelected.contains("image_$index")) }
    val (backgroundColor, alpha) = if (selected.value) {
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
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.app_icon_background), contentDescription = ""
            )
        }

        AssetImageIndicator(index, selected.value, assetSelected) { isSelected ->
            if (assetSelected.size == config.maxAssets && isSelected) {
                context.showShortToast("已经达到最大值${config.maxAssets}了")
                return@AssetImageIndicator
            }
            selected.value = !selected.value
            if (isSelected) {
                assetSelected += "image_$index"
            } else {
                assetSelected -= "image_$index"
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetImageIndicator(
    index: Int,
    selected: Boolean,
    assetSelected: SnapshotStateList<String>,
    onClick: (Boolean) -> Unit
) {
    Surface(
        onClick = { onClick(!selected) },
        modifier = Modifier
            .padding(6.dp)
            .size(size = 28.dp)
            .clickable { onClick(false) },
        shape = CircleShape,
        border = if (!selected) BorderStroke(width = 1.dp, color = Color.White) else null,
        color = if (selected) Color(64, 151, 246) else Color(0f, 0f, 0f, 0.3F)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (selected) {
                val num = assetSelected.indexOf("image_$index") + 1
                Text(
                    text = "${if (selected) num else null}",
                    color = Color.White
                )
            }
        }
    }
}

