package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.huhx.picker.data.MomentModelFactory
import com.huhx.picker.data.MomentRepository
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.ui.theme.Compose_image_pickerTheme

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_image_pickerTheme() {
                val navController = rememberAnimatedNavController()
                val viewModel: MomentViewModel = viewModel(
                    factory = MomentModelFactory(momentRepository = MomentRepository())
                )
                AppRoute(navController = navController, viewModel = viewModel)
            }
        }
    }
}