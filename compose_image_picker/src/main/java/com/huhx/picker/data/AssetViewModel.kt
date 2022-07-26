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
    private val _directoryGroup = mutableStateListOf<AssetDirectory>()

    val directoryGroup: List<AssetDirectory>
        get() = _directoryGroup

    val selectedList = mutableStateListOf<AssetInfo>()
    val expanded = mutableStateOf(false)
    val folderName = mutableStateOf("所有项目")

    fun initAssets() {
        viewModelScope.launch {
            initAssets(RequestType.COMMON)
        }
    }

    fun initDirectories() {
        viewModelScope.launch {
            initDirectories(RequestType.COMMON)
        }
    }

    private fun initDirectories(requestType: RequestType) {
        val assetList = getAssets(requestType)
        val directoryList = assetList.groupBy {
            it.directory
        }.map {
            AssetDirectory(directory = it.key, assets = it.value)
        }
        _directoryGroup.clear()
        _directoryGroup.add(AssetDirectory(directory = "所有项目", assets = assetList))
        _directoryGroup.addAll(directoryList)
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
        navController.navigate("preview?uri=${assetInfo.uriString}&isVideo=${assetInfo.isVideo()}")
    }

    val selectedText: String
        get() = if (isEnable) "确定(${selectedList.size}/${assetPickerConfig.maxAssets})" else "确定"
}