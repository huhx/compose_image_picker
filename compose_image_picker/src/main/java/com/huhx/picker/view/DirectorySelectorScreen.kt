package com.huhx.picker.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.huhx.picker.R
import com.huhx.picker.data.AssetDirectory
import com.huhx.picker.data.AssetInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectorySelectorScreen(
    directory: String,
    selectedList: List<AssetInfo>,
    assetDirectories: List<AssetDirectory>,
    navController: NavHostController,
    onPicked: (List<AssetInfo>) -> Unit,
    onClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            DirectoryTopAppBar(
                directory = directory,
                selectedList = selectedList,
                navigateUp = { navController.navigateUp() },
                onPicked = onPicked,
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            DirectorySelector(
                directory = directory,
                assetDirectories = assetDirectories,
                onClick = onClick
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoryTopAppBar(
    directory: String,
    selectedList: List<AssetInfo>,
    navigateUp: () -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = { NavigationIcon(navigateUp) },
        title = {
            Row(modifier = Modifier.clickable(onClick = navigateUp)) {
                Text(directory, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowUp, "")
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DirectorySelector(
    directory: String,
    assetDirectories: List<AssetDirectory>,
    onClick: (String) -> Unit,
) {
    LazyColumn {
        items(assetDirectories) {
            val itemDirectory = it.directory
            ListItem(
                modifier = Modifier.clickable { onClick(itemDirectory) },
                icon = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.cover ?: R.drawable.app_icon_background)
                            .decoderFactory(VideoFrameDecoder.Factory())
                            .build(),
                        modifier = Modifier
                            .size(32.dp)
                            .aspectRatio(1.0F),
                        filterQuality = FilterQuality.None,
                        contentScale = ContentScale.Crop,
                        contentDescription = ""
                    )
                },
                text = {
                    Row {
                        Text(text = itemDirectory)
                        Text(text = "(${it.counts})", color = Color.Gray)
                    }
                },
                trailing = { TrailingIcon(directory, itemDirectory) }
            )
        }
    }
}