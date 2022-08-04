package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.huhx.picker.data.MomentModelFactory
import com.huhx.picker.data.MomentRepository
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.ui.theme.Compose_image_pickerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_image_pickerTheme() {
                val navController = rememberAnimatedNavController()
                val viewModel: MomentViewModel = viewModel(
                    factory = MomentModelFactory(momentRepository = MomentRepository())
                )

                Scaffold { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppRoute(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}