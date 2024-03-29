package com.huhx.picker

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.AssetPickerConfig
import com.huhx.picker.support.PickerPermissions
import com.huhx.picker.view.AssetPicker
import com.huhx.picker.view.MomentAddScreen
import com.huhx.picker.view.MomentListScreen

@Composable
fun AppRoute(
    navController: NavHostController,
    viewModel: MomentViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = "asset_picker",
    ) {
        composable("moment_list") {
            MomentListScreen(viewModel) { navController.navigate("moment_add") }
        }

        composable("moment_add") {
            MomentAddScreen(viewModel, navController, navController::navigateUp)
        }

        composable("asset_picker") {
            ImagePicker(
                onPicked = {
                    viewModel.selectedList.clear()
                    viewModel.selectedList.addAll(it)
                    navController.navigateUp()
                },
                onClose = {
                    viewModel.selectedList.clear()
                    navController.navigateUp()
                }
            )
        }
    }
}

@Composable
private fun ImagePicker(
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    PickerPermissions(permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
        AssetPicker(
            assetPickerConfig = AssetPickerConfig(gridCount = 3),
            onPicked = onPicked,
            onClose = onClose
        )
    }
}