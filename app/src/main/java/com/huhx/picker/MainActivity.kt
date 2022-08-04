package com.huhx.picker

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.constant.showShortToast
import com.huhx.picker.data.PickerPermissions
import com.huhx.picker.ui.theme.Compose_image_pickerTheme
import com.huhx.picker.view.AssetPicker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            Compose_image_pickerTheme() {
                PickerPermissions(permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                    AssetPicker(
                        assetPickerConfig = AssetPickerConfig(gridCount = 3),
                        onPicked = {
                            context.showShortToast("picked size = ${it.size}")
                        },
                        onClose = {
                            context.showShortToast("close size = ${it.size}")
                        }
                    )
                }
            }
        }
    }
}