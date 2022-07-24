package com.huhx.picker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx.picker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QQAssetPicker() {
    Scaffold(topBar = { TopAppBar() }, bottomBar = { BottomAppBar() }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            QQAssetContent()
        }
    }
}

@Composable
fun TopAppBar() {
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
        title = { Text("所有项目", fontSize = 16.sp) },
    )
}

@Composable
fun BottomAppBar() {

}

@Composable
fun QQAssetContent() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 3.dp, vertical = 3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        userScrollEnabled = true
    ) {
        items(100) { index ->
            AssetImage(index)
        }
    }
}

@Composable
fun AssetImage(index: Int) {
    Box(contentAlignment = Alignment.TopEnd) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.app_icon_background), contentDescription = ""
        )
        AssetImageIndicator(index)
    }
}

@Composable
fun AssetImageIndicator(index: Int) {
    val selected = remember { mutableStateOf(index % 2 == 0) }

    if (selected.value) {
        Text(
            modifier = Modifier
                .padding(6.dp)
                .clickable { selected.value = !selected.value },
            text = index.toString(),
            textAlign = TextAlign.End
        )
    } else {
        Text(
            modifier = Modifier
                .padding(6.dp)
                .clickable { selected.value = !selected.value },
            text = "0",
            textAlign = TextAlign.End
        )
    }
}