package com.huhx.picker.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.constant.RequestType
import kotlinx.coroutines.launch

class AssetViewModel constructor(
    private val assetPickerRepository: AssetPickerRepository,
    val assetPickerConfig: AssetPickerConfig
) : ViewModel() {

    val assets = mutableStateListOf<AssetInfo>()
    val selectedList = mutableStateListOf<AssetInfo>()
    val expanded = mutableStateOf(false)
    val folderName = mutableStateOf("所有项目")

    fun initAssets() {
        viewModelScope.launch {
            getAssets(RequestType.COMMON)
        }
    }

    private suspend fun getAssets(requestType: RequestType) {
        assets.clear()
        assets.addAll(assetPickerRepository.getAssets(requestType))
    }

    fun isFullSelected(): Boolean {
        return selectedList.size == assetPickerConfig.maxAssets
    }

    fun add(assetInfo: AssetInfo) {
        selectedList += assetInfo
    }

    fun remove(assetInfo: AssetInfo) {
        selectedList -= assetInfo
    }

    fun isSelected(assetInfo: AssetInfo): Boolean {
        return selectedList.any {
            it.id == assetInfo.id
        }
    }

    val isEnable = selectedList.size > 0

    val selectedText: String
        get() = if (isEnable) "确定(${selectedList.size}/${assetPickerConfig.maxAssets})" else "确定"
}