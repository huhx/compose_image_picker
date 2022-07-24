package com.huhx.picker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.R
import com.huhx.picker.constant.showShortToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QQAssetPicker() {
    Scaffold(
        topBar = { TopAppBar() },
        bottomBar = { BottomAppBar() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            TabView()
        }
    }
}

@Composable
fun TopAppBar() {
    val folderName = remember { mutableStateOf("所有项目") }

    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        },
        title = { Text(folderName.value, fontSize = 16.sp) },
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
fun BottomAppBar() {

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