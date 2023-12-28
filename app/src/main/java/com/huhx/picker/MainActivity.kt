package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.huhx.picker.component.AssetImageItem
import com.huhx.picker.data.MomentModelFactory
import com.huhx.picker.data.MomentRepository
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.model.AssetResourceType
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

@Composable
@Preview
fun AssetImageItemPreview() {
    Compose_image_pickerTheme {
        AssetImageItem(
            urlString = "https://huhx-family.oss-cn-beijing.aliyuncs.com/20220116082003904824.jpg",
            isSelected = true,
            resourceType = AssetResourceType.IMAGE,
            durationString = "00:15",
            navigateToPreview = {}
        )
    }
}