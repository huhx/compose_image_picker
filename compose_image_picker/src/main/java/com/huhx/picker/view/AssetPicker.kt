package com.huhx.picker.view

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.huhx.picker.constant.showShortToast
import kotlinx.coroutines.launch
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QQAssetPicker(
    onPicked: (List<String>) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val folderName = remember { mutableStateOf("所有项目") }
    val navController = rememberAnimatedNavController()
    val isHome = currentRoute(navController) == "home"

    Scaffold(
        topBar = {
            TopAppBar(
                expanded = expanded,
                folderName = folderName,
                navigateUp = {
                    if (isHome) {
                        onPicked(listOf("abc", "efg"))
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AssetPickerRoute(navController = navController) {
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
    navigateBack: (String) -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            TabView()
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
    navigateUp: () -> Unit,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<String>) -> Unit
) {
    val selectedCount = remember { mutableStateOf(2) }
    val isEnable = selectedCount.value > 0
    val text = if (isEnable) "确定(${selectedCount.value}/9)" else "确定"

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
                onClick = { onPicked(listOf("abc", "efg")) }
            ) {
                Text(text)
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabView() {
    val tabs = listOf(
        TabItem.All,
        TabItem.Video,
        TabItem.Image,
    )
    val pagerState = rememberPagerState()
    Column {
        Tab(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState)
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
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}

@Composable
fun QQAssetContent() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        userScrollEnabled = true
    ) {
        item {
            AssetCamera()
        }
        items(100) { index ->
            AssetImage(index)
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
fun AssetImage(index: Int) {
    val selected = remember { mutableStateOf(index % 2 == 0) }
    val backgroundColor = if (selected.value) Color.Black else Color.Transparent
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .alpha(0.5F),
        contentAlignment = Alignment.TopEnd,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.app_icon_background), contentDescription = ""
        )

        AssetImageIndicator(index, selected.value) {
            selected.value = !selected.value
            context.showShortToast("selected is ${selected.value}")
        }
    }
}

@Composable
fun AssetImageIndicator(
    index: Int,
    selected: Boolean,
    onClick: () -> Unit
) {

    if (selected) {
        Text(
            modifier = Modifier
                .padding(6.dp)
                .clickable { onClick() },
            text = index.toString(),
            color = Color.White,
        )
    } else {
        Text(
            modifier = Modifier
                .padding(6.dp)
                .clickable { onClick() },
            text = "0",
            color = Color.White,
        )
    }
}