package com.huhx.picker.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.constant.RequestType
import kotlinx.coroutines.launch

class AssetViewModel constructor(
    private val assetPickerRepository: AssetPickerRepository,
    val assetPickerConfig: AssetPickerConfig,
    private val navController: NavController,
) : ViewModel() {

    private val assets = mutableStateListOf<AssetInfo>()
    val selectedList = mutableStateListOf<AssetInfo>()
    val expanded = mutableStateOf(false)
    val folderName = mutableStateOf("所有项目")

    fun initAssets() {
        viewModelScope.launch {
            initAssets(RequestType.COMMON)
        }
    }

    private suspend fun initAssets(requestType: RequestType) {
        assets.clear()
        assets.addAll(assetPickerRepository.getAssets(requestType))
    }

    fun getAssets(requestType: RequestType): List<AssetInfo> {
        return when (requestType) {
            RequestType.COMMON -> assets
            RequestType.IMAGE -> assets.filter(AssetInfo::isImage)
            RequestType.VIDEO -> assets.filter(AssetInfo::isVideo)
        }
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

    fun navigateToPreview(assetInfo: AssetInfo) {
        navController.navigate("preview?uri=${assetInfo.uri}&isVideo=${assetInfo.isVideo()}")
    }

    val selectedText: String
        get() = if (isEnable) "确定(${selectedList.size}/${assetPickerConfig.maxAssets})" else "确定"
}