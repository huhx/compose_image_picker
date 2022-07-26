package com.huhx.picker.constant

import android.content.Context
import android.widget.Toast

fun Context.showShortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Long.prefixZero(): String {
    return if (this < 9) "0$this" else "$this"
}