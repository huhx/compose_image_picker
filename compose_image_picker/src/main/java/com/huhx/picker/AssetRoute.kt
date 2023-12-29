package com.huhx.picker

import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.RequestType
import com.huhx.picker.view.AssetDisplayScreen
import com.huhx.picker.view.AssetPreviewScreen
import com.huhx.picker.view.AssetSelectorScreen
import com.huhx.picker.viewmodel.AssetViewModel

@UnstableApi @Composable
internal fun AssetPickerRoute(
    navController: NavHostController,
    viewModel: AssetViewModel,
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    NavHost(navController = navController, startDestination = AssetRoute.display) {
        composable(AssetRoute.display) {
            AssetDisplayScreen(
                viewModel = viewModel,
                navigateToDropDown = { navController.navigate(AssetRoute.selector(it)) },
                onPicked = onPicked,
                onClose = onClose,
            )
        }

        composable(
            AssetRoute.selector,
            arguments = listOf(navArgument("directory") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            val directory = arguments.getString("directory")!!

            AssetSelectorScreen(
                directory = directory,
                assetDirectories = viewModel.directoryGroup,
                navigateUp = { navController.navigateUp() },
                onSelected = { name ->
                    navController.navigateUp()
                    viewModel.directory = name
                },
            )
        }

        composable(
            AssetRoute.preview,
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("requestType") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            val index = arguments.getInt("index")
            val requestType = arguments.getString("requestType")
            val assets = viewModel.getAssets(RequestType.valueOf(requestType!!))

            AssetPreviewScreen(
                index = index,
                assets = assets,
                selectedList = viewModel.selectedList,
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}

object AssetRoute {
    const val display = "asset_display"
    const val preview = "asset_preview?index={index}&requestType={requestType}"
    const val selector = "asset_selector?directory={directory}"

    fun display(): String = display

    fun preview(index: Int, requestType: RequestType): String {
        return preview.replaceFirst("{index}", index.toString())
            .replaceFirst("{requestType}", requestType.name)
    }

    fun selector(directory: String): String {
        return selector.replaceFirst("{directory}", directory)
    }
}