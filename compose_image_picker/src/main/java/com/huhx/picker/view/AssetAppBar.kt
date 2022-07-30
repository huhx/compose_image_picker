package com.huhx.picker.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx.picker.data.AssetInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    directory: String,
    selectedList: List<AssetInfo>,
    navigateUp: () -> Unit,
    navigateToDropDown: () -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = { NavigationIcon(navigateUp) },
        title = {
            Row(modifier = Modifier.clickable { navigateToDropDown() }) {
                Text(directory, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowDown, "")
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

@Composable
private fun NavigationIcon(navigateUp: () -> Unit) {
    IconButton(onClick = navigateUp) {
        Icon(
            Icons.Filled.Close,
            tint = Color.Black,
            contentDescription = "",
        )
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
            Row(modifier = Modifier.clickable { navigateUp() }) {
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

@Composable
private fun AppBarButton(
    size: Int,
    onPicked: () -> Unit
) {
    val isEnabled = size > 0
    Button(
        modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        onClick = { onPicked() }
    ) {
        Text(if (isEnabled) "确定($size/9)" else "确定")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewTopAppBar(navigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        ),
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "",
                )
            }
        }
    )
}