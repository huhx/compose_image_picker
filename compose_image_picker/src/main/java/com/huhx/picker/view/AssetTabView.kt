package com.huhx.picker.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.R
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    viewModel: AssetViewModel,
    navController: NavHostController,
    onPicked: (List<AssetInfo>) -> Unit,
) {
    Scaffold(
        topBar = {
            val directory = viewModel.directory.value
            HomeTopAppBar(
                directory = directory,
                selectedList = viewModel.selectedList,
                navigateUp = { },
                onPicked = onPicked,
                navigateToDropDown = { navController.navigate("dropDown?directory=$directory") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            TabView(viewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabView(
    viewModel: AssetViewModel,
) {
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

sealed class TabItem(
    @StringRes val resourceId: Int,
    val screen: @Composable (AssetViewModel) -> Unit
) {
    object All : TabItem(R.string.tab_item_all, { viewModel ->
        AssetAll(viewModel)
    })

    object Video : TabItem(R.string.tab_item_video, { viewModel ->
        AssetVideo(viewModel)
    })

    object Image : TabItem(R.string.tab_item_image, { viewModel ->
        AssetImage(viewModel)
    })
}
