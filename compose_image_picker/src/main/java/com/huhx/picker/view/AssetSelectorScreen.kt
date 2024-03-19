package com.huhx.picker.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.huhx.picker.model.AssetDirectory

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AssetSelectorScreen(
    directory: String,
    assetDirectories: List<AssetDirectory>,
    navigateUp: () -> Unit,
    onSelected: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.Filled.Close, contentDescription = "")
                    }
                },
                title = {
                    Row(modifier = Modifier.clickable(onClick = navigateUp)) {
                        Text(text = directory, fontSize = 18.sp)
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items = assetDirectories) {
                val itemDirectory = it.directory
                ListItem(
                    modifier = Modifier.clickable { onSelected(itemDirectory) },
                    leadingContent = {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.cover ?: Icons.Default.Place)
                                .decoderFactory(VideoFrameDecoder.Factory())
                                .build(),
                            modifier = Modifier
                                .size(32.dp)
                                .aspectRatio(1.0F),
                            filterQuality = FilterQuality.Low,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    },
                    headlineContent = {
                        Row {
                            Text(text = itemDirectory, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = "(${it.counts})", color = Color.Gray)
                        }
                    },
                    trailingContent = {
                        if (directory == itemDirectory) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "", tint = Color.Blue)
                        }
                    }
                )
            }
        }
    }
}
