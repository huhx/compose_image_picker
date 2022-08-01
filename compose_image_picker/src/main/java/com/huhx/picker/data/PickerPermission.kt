package com.huhx.picker.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickerPermission(
    permissions: List<String>,
    content: @Composable () -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(permissions)
    if (permissionState.allPermissionsGranted) {
        content()
    } else {
        LaunchedEffect(key1 = Unit, block = {
            permissionState.launchMultiplePermissionRequest()
        })
    }
}