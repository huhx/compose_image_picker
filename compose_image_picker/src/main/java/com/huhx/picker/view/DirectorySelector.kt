package com.huhx.picker.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.huhx.picker.R
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectorySelectorScreen(
    directory: String,
    viewModel: AssetViewModel,
    navController: NavHostController,
    onPicked: (List<AssetInfo>) -> Unit,
    onClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            DirectoryTopAppBar(
                directory = directory,
                selectedList = viewModel.selectedList,
                navigateUp = { navController.navigateUp() },
                onPicked = { onPicked(it) },
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            DirectorySelector(directory = directory, viewModel = viewModel, onClick = { onClick(it) })
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DirectorySelector(
    directory: String,
    viewModel: AssetViewModel,
    onClick: (String) -> Unit,
) {
    LazyColumn {
        items(viewModel.directoryGroup) {
            ListItem(
                modifier = Modifier.clickable { onClick(it.directory) },
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
                        Text(text = it.directory)
                        Text(text = "(${it.counts})", color = Color.Gray)
                    }
                },
                trailing = { TrailingIcon(directory, it.directory) }
            )
        }
    }
}

@Composable
fun TrailingIcon(source: String, target: String) {
    if (source == target) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "",
            tint = Color.Blue
        )
    }
}