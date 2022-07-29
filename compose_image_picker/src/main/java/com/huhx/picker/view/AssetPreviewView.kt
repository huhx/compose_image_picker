package com.huhx.picker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.huhx.picker.constant.showShortToast
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun AssetPreviewView(
    index: Int,
    assets: List<AssetInfo>,
    viewModel: AssetViewModel,
    previewBack: () -> Unit,
) {
    val pageState = rememberPagerState(initialPage = index)

    Scaffold(
        bottomBar = {
            SelectorBottomBar(pageState, assets, viewModel) {
                previewBack()
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SelectorBottomBar(
    pagerState: PagerState,
    assets: List<AssetInfo>,
    viewModel: AssetViewModel,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val assetInfo = assets[pagerState.currentPage]

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
                selected = viewModel.selectedList.any { it == assetInfo },
                assetSelected = viewModel.selectedList,
            ) { isSelected ->
                if (viewModel.isFullSelected() && isSelected) {
                    context.showShortToast("已经达到最大值${viewModel.assetPickerConfig.maxAssets}了")
                    return@AssetImageIndicator
                }
                if (isSelected) {
                    viewModel.add(assetInfo)
                } else {
                    viewModel.remove(assetInfo)
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "选择", color = Color.White)
        }
        Button(
            modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            onClick = { onClick() }
        ) {
            Text("确定", color = Color.White)
        }
    }
}