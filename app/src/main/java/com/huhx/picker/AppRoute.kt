package com.huhx.picker

import android.Manifest
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.data.AssetInfo
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.data.PickerPermissions
import com.huhx.picker.view.AssetPicker
import com.huhx.picker.view.MomentAddScreen
import com.huhx.picker.view.MomentListScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppRoute(
    navController: NavHostController,
    viewModel: MomentViewModel
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "moment_list",
    ) {
        composable("moment_list") {
            MomentListScreen(viewModel) {
                navController.navigate("moment_add")
            }
        }


        composable("moment_add") {
            MomentAddScreen(viewModel, navController) {
                navController.navigateUp()
            }
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