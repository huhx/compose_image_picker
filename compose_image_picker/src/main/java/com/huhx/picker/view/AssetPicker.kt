package com.huhx.picker.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.AssetPickerRepository
import com.huhx.picker.data.AssetPickerRoute
import com.huhx.picker.data.AssetViewModel
import com.huhx.picker.data.AssetViewModelFactory
import com.huhx.picker.data.currentRoute

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QQAssetPicker(
    assetPickerConfig: AssetPickerConfig,
    onPicked: (List<AssetInfo>) -> Unit,
) {
    val context = LocalContext.current
    val navController = rememberAnimatedNavController()
    val viewModel: AssetViewModel = viewModel(
        factory = AssetViewModelFactory(
            assetPickerRepository = AssetPickerRepository(context),
            assetPickerConfig = assetPickerConfig,
            navController = navController
        )
    )

    LaunchedEffect(Unit, block = {
        viewModel.initDirectories()
    })

    val isHome = currentRoute(navController) == "home"

    Scaffold(
        topBar = { AssetTopBar(viewModel, isHome, onPicked, navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AssetPickerRoute(
                navController = navController,
                viewModel = viewModel,
            ) {
                navController.navigateUp()
                viewModel.toggle()
                viewModel.updateDirectory(it)
            }
        }
    }
}
