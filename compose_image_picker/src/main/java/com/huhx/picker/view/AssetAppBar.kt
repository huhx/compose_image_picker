package com.huhx.picker.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
fun AssetTopBar(
    viewModel: AssetViewModel,
    isHome: Boolean,
    onPicked: (List<AssetInfo>) -> Unit,
    navController: NavHostController
) {
    val isPreview = currentRoute(navController)?.startsWith("preview")
    if (isPreview != true) {
        TopAppBar(
            viewModel = viewModel,
            navigateUp = {
                if (isHome) {
                    onPicked(viewModel.selectedList)
                } else {
                    navController.navigateUp()
                }
            },
            navigateToDropDown = {
                navController.navigate("dropDown?directory=$it")
            },
            onPicked = { onPicked(it) }
        )
    }
}


@Composable
fun TopAppBar(
    viewModel: AssetViewModel,
    navigateUp: () -> Unit,
    navigateToDropDown: (String) -> Unit,
    onPicked: (List<AssetInfo>) -> Unit
) {
    val folderName = viewModel.folderName.value
    val expanded = viewModel.expanded

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
            Row(
                modifier = Modifier.clickable {
                    expanded.value = !expanded.value
                    if (expanded.value) navigateToDropDown(folderName) else navigateUp()
                }
            ) {
                Text(folderName, fontSize = 18.sp)
                if (expanded.value) {
                    Icon(Icons.Default.KeyboardArrowUp, "")
                } else {
                    Icon(Icons.Default.KeyboardArrowDown, "")
                }
            }
        },
        actions = {
            Button(
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                enabled = viewModel.isEnable,
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = { onPicked(viewModel.selectedList) }
            ) {
                Text(viewModel.selectedText)
            }
        }
    )
}