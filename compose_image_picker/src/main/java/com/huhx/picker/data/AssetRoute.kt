package com.huhx.picker.data

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.huhx.picker.constant.RequestType
import com.huhx.picker.view.AssetPreviewView
import com.huhx.picker.view.DirectorySelector
import com.huhx.picker.view.TabView

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AssetPickerRoute(
    navController: NavHostController,
    viewModel: AssetViewModel,
    onPicked: (List<AssetInfo>) -> Unit,
    navigateBack: (String) -> Unit,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            TabView(viewModel)
        }

        composable(
            "dropDown?directory={directory}",
            arguments = listOf(navArgument("directory") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            val directory = arguments.getString("directory")!!
            DirectorySelector(directory, viewModel) { navigateBack(it) }
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
            AssetPreviewView(index, assets, viewModel) {
                navController.navigateUp()
                onPicked(viewModel.selectedList)
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}