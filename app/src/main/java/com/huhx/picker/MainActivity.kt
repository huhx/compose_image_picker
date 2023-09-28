package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.huhx.picker.data.MomentModelFactory
import com.huhx.picker.data.MomentRepository
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.ui.theme.Compose_image_pickerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_image_pickerTheme {
                val navController = rememberNavController()
                val viewModel: MomentViewModel = viewModel(
                    factory = MomentModelFactory(momentRepository = MomentRepository())
                )
                AppRoute(navController = navController, viewModel = viewModel)
            }
        }
    }
}