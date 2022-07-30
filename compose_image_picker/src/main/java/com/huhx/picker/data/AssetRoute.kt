package com.huhx.picker.data

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.huhx.picker.constant.RequestType
import com.huhx.picker.view.AssetPreviewScreen
import com.huhx.picker.view.DirectorySelectorScreen
import com.huhx.picker.view.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AssetPickerRoute(
    navController: NavHostController,
    viewModel: AssetViewModel,
    onPicked: (List<AssetInfo>) -> Unit,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                navController = navController,
                onPicked = onPicked
            )
        }

        composable(
            "dropDown?directory={directory}",
            arguments = listOf(navArgument("directory") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            val directory = arguments.getString("directory")!!
            DirectorySelectorScreen(
                directory = directory,
                selectedList = viewModel.selectedList,
                assetDirectories = viewModel.directoryGroup,
                navController = navController,
                onClick = { name ->
                    navController.navigateUp()
                    viewModel.directory = name
                },
                onPicked = onPicked,
                )
        }

        composable(
            "preview?index={index}&requestType={requestType}",
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
                navController = navController,
                viewModel = viewModel,
                onPicked = onPicked
            )
        }
    }
}