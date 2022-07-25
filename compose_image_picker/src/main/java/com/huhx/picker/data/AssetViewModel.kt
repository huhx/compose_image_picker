package com.huhx.picker.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.huhx.picker.constant.AssetPickerConfig

class AssetViewModel constructor(
    val assetPickerConfig: AssetPickerConfig
) : ViewModel() {

    val selectedList = mutableStateListOf<String>()
    val expanded = mutableStateOf(false)
    val folderName = mutableStateOf("所有项目")

    fun isFullSelected(): Boolean {
        return selectedList.size == assetPickerConfig.maxAssets
    }

    fun add(index: Int) {
        selectedList += "image_$index"
    }

    fun remove(index: Int) {
        selectedList -= "image_$index"
    }

    fun isSelected(index: Int): Boolean {
        return selectedList.contains("image_$index")
    }

    val isEnable = selectedList.size > 0

    val selectedText: String
        get() = if (isEnable) "确定(${selectedList.size}/${assetPickerConfig.maxAssets})" else "确定"
}