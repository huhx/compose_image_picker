package com.huhx.picker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CircleNetworkImage(
    imageName: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageName,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun GridViewImages(images: List<String>) {
    LazyVerticalGrid(
        modifier = Modifier.heightIn(0.dp, 600.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 3.dp, vertical = 3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        userScrollEnabled = false
    ) {
        items(images, key = { it }) { image ->
            AsyncImage(
                model = image,
                modifier = Modifier.aspectRatio(1f),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}