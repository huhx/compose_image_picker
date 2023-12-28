package com.huhx.picker.support

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PickerPermission(permission: String, content: @Composable () -> Unit) {
    val context = LocalContext.current
    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    val permissionState = rememberPermissionState(permission) { permissionRequested = true }
    if (permissionState.status.isGranted) return content()

    if (!permissionRequested && !permissionState.status.shouldShowRationale) {
        SideEffect(permissionState::launchPermissionRequest)
    } else if (permissionRequested && permissionState.status.shouldShowRationale) {
        SideEffect(permissionState::launchPermissionRequest)
    } else {
        goToSetting(context)
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PickerPermissions(permissions: List<String>, content: @Composable () -> Unit) {
    val context = LocalContext.current
    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    val permissionState = rememberMultiplePermissionsState(permissions) { permissionRequested = true }
    if (permissionState.allPermissionsGranted) return content()

    if (!permissionRequested && !permissionState.shouldShowRationale) {
        SideEffect(permissionState::launchMultiplePermissionRequest)
    } else if (permissionRequested && permissionState.shouldShowRationale) {
        SideEffect(permissionState::launchMultiplePermissionRequest)
    } else {
        goToSetting(context)
    }
}


private fun goToSetting(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}