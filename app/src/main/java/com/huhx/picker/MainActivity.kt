package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
@Preview(showSystemUi = true)
fun AssetImageItemPreview() {
    Compose_image_pickerTheme {
        LazyVerticalGrid(
            modifier = Modifier.heightIn(0.dp, 600.dp),
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            columns = GridCells.Fixed(3),
            userScrollEnabled = false
        ) {
            items(12) {
                AssetImageItem(
                    urlString = "https://huhx-family.oss-cn-beijing.aliyuncs.com/20220116082003904824.jpg",
                    isSelected = false,
                    resourceType = AssetResourceType.VIDEO,
                    durationString = "00:15",
                    onDelete = {},
                    navigateToPreview = {}
                )
            }
        }
    }
}