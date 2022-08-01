package com.huhx.picker.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.huhx.picker.constant.goSetting

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickerPermission(
    permission: String,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    val permissionState = rememberPermissionState(permission) {
        permissionRequested = true
    }

    if (permissionState.status.isGranted) {
        content()
    } else {
        if (!permissionRequested && !permissionState.status.shouldShowRationale) {
            SideEffect {
                permissionState.launchPermissionRequest()
            }
        } else if (permissionRequested && permissionState.status.shouldShowRationale) {
            SideEffect {
                permissionState.launchPermissionRequest()
            }
        } else {
            context.goSetting()
        }
    }
}
