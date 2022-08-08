package com.huhx.picker.constant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.format.Formatter
import android.widget.Toast

internal fun Context.showShortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

internal fun Context.goSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${this.packageName}"))
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

internal fun Long.prefixZero(): String {
    return if (this < 10) "0$this" else "$this"
}

internal fun Context.formatSize(size: Long): String {
    return Formatter.formatFileSize(this, size)
}