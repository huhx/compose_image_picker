package com.huhx.picker.view

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.huhx.picker.AssetPickerRoute
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.AssetPickerConfig
import com.huhx.picker.provider.AssetPickerRepository
import com.huhx.picker.viewmodel.AssetViewModel
import com.huhx.picker.viewmodel.AssetViewModelFactory

@UnstableApi
@Composable
fun AssetPicker(
    assetPickerConfig: AssetPickerConfig,
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
    onLoading: @Composable (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val viewModel: AssetViewModel = viewModel(
        factory = AssetViewModelFactory(
            assetPickerRepository = AssetPickerRepository(context),
            navController = navController
        )
    )
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.initDirectories()
        isLoading.value = false
    }

    if (isLoading.value) {
        onLoading?.invoke() ?: CircularProgressIndicator()
    } else {
        CompositionLocalProvider(LocalAssetConfig provides assetPickerConfig) {
            AssetPickerRoute(
                navController = navController,
                viewModel = viewModel,
                onPicked = onPicked,
                onClose = onClose,
            )
        }
    }
}
