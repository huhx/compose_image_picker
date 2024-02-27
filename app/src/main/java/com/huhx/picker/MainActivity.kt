package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.huhx.picker.component.AssetImageItem
import com.huhx.picker.data.MomentModelFactory
import com.huhx.picker.data.MomentRepository
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.AssetResourceType
import com.huhx.picker.ui.theme.Compose_image_pickerTheme
import com.huhx.picker.view.AssetImageIndicator
import com.huhx.picker.view.SelectorBottomBar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_image_pickerTheme {
                val navController = rememberNavController()
                val viewModel: MomentViewModel = viewModel(
                    factory = MomentModelFactory(momentRepository = MomentRepository())
                )
                AppRoute(navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AssetImageItemPreview() {
    Compose_image_pickerTheme {
        LazyVerticalGrid(
            modifier = Modifier.heightIn(0.dp, 600.dp),
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            columns = GridCells.Fixed(3),
            userScrollEnabled = false
        ) {
            items(12) {
                AssetImageItem(
                    urlString = "https://huhx-family.oss-cn-beijing.aliyuncs.com/20220116082003904824.jpg",
                    isSelected = false,
                    resourceType = AssetResourceType.VIDEO,
                    durationString = "00:15",
                    onDelete = {},
                    navigateToPreview = {}
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AssetImageItemPreview2() {
    val assetInfo = AssetInfo(
        id = 8L,
        filepath = "/data/emulator/test.jpeg",
        uriString = "http://lcoalhost/5",
        filename = "test.jpeg",
        directory = "Picture",
        mediaType = 1,
        size = 1150260,
        mimeType = "img/jpeg",
        duration = 16000,
        date = 23423434433
    )

    val list = SnapshotStateList<AssetInfo>()
    list.add(assetInfo)
    list.add(assetInfo)

    Compose_image_pickerTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black.copy(alpha = 0.9F))
                .padding(horizontal = 10.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AssetImageIndicator(
                    assetInfo = assetInfo,
                    size = 20.dp,
                    fontSize = 14.sp,
                    selected = true,
                    assetSelected = list
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.text_asset_select), color = Color.White, fontSize = 14.sp)
            }
            Button(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                onClick = { }
            ) {
                Text(stringResource(R.string.text_done), color = Color.White, fontSize = 15.sp)
            }
        }
    }
}

@Preview
@Composable
fun AssetImageIndicatorPreview() {
    val assetInfo = AssetInfo(
        id = 8L,
        filepath = "/data/emulator/test.jpeg",
        uriString = "http://lcoalhost/5",
        filename = "test.jpeg",
        directory = "Picture",
        mediaType = 1,
        size = 1150260,
        mimeType = "img/jpeg",
        duration = 16000,
        date = 23423434433
    )

    val list = SnapshotStateList<AssetInfo>()
    list.add(assetInfo)
    list.add(assetInfo)

    Compose_image_pickerTheme {
        AssetImageIndicator(
            assetInfo = assetInfo,
            size = 24.dp,
            fontSize = 15.sp,
            selected = true,
            assetSelected = list
        )
    }
}

@Preview
@Composable
fun AssetPreviewScreenPreview() {
    val assetInfo = AssetInfo(
        id = 8L,
        filepath = "https://huhx-family.oss-cn-beijing.aliyuncs.com/20220116082003904824.jpg",
        uriString = "https://huhx-family.oss-cn-beijing.aliyuncs.com/20220116082003904824.jpg",
        filename = "test.jpeg",
        directory = "Picture",
        mediaType = 1,
        size = 1150260,
        mimeType = "img/jpeg",
        duration = 16000,
        date = 23423434433
    )
    val list = listOf(assetInfo, assetInfo)
    val selectedList = SnapshotStateList<AssetInfo>()
    selectedList.add(assetInfo)
    selectedList.add(assetInfo)

    Compose_image_pickerTheme {
        SelectorBottomBar(assetInfo = assetInfo, selectedList = selectedList, onClick = {})
    }
}