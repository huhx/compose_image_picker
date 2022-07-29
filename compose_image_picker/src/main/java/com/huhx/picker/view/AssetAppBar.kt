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
import androidx.navigation.NavHostController
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetViewModel
import com.huhx.picker.data.currentRoute

@Composable
internal fun AssetTopBar(
    viewModel: AssetViewModel,
    onPicked: (List<AssetInfo>) -> Unit,
    navController: NavHostController
) {
    val currentRoute = currentRoute(navController)
    if (currentRoute == "home") {
        HomeTopAppBar(
            viewModel = viewModel,
            navigateUp = { onPicked(viewModel.selectedList) },
            onPicked = { onPicked(it) },
            navigateToDropDown = { navController.navigate("dropDown?directory=$it") }
        )
    }

    if (currentRoute?.startsWith("dropDown") == true) {
        DirectoryTopAppBar(
            viewModel = viewModel,
            navigateUp = { navController.navigateUp() },
            onPicked = { onPicked(it) },
        )
    }

    if (currentRoute?.startsWith("preview") == true) {
        PreviewTopAppBar(navigateUp = { navController.navigateUp() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    viewModel: AssetViewModel,
    navigateUp: () -> Unit,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    val directory = viewModel.directory.value

    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    Icons.Filled.Close,
                    tint = Color.Black,
                    contentDescription = "",
                )
            }
        },
        title = {
            Row(modifier = Modifier.clickable { navigateToDropDown(directory) }) {
                Text(directory, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowDown, "")
            }
        },
        actions = {
            Button(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                enabled = viewModel.selectedList.size > 0,
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = { onPicked(viewModel.selectedList) }
            ) {
                Text(viewModel.selectedText)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoryTopAppBar(
    viewModel: AssetViewModel,
    navigateUp: () -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    val directory = viewModel.directory.value

    CenterAlignedTopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    Icons.Filled.Close,
                    tint = Color.Black,
                    contentDescription = "",
                )
            }
        },
        title = {
            Row(modifier = Modifier.clickable { navigateUp() }) {
                Text(directory, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowUp, "")
            }
        },
        actions = {
            Button(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                enabled = viewModel.selectedList.size > 0,
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = { onPicked(viewModel.selectedList) }
            ) {
                Text(viewModel.selectedText)
            }
        }
    )
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
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "",
                )
            }
        }
    )
}