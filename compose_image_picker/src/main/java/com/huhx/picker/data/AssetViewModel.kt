package com.huhx.picker.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var directory by mutableStateOf("Photos/Videos")

    fun initDirectories() {
        viewModelScope.launch {
            initAssets(RequestType.COMMON)
            val directoryList = assets.groupBy {
                it.directory
            }.map {
                AssetDirectory(directory = it.key, assets = it.value)
            }
            _directoryGroup.clear()
            _directoryGroup.add(AssetDirectory(directory = "Photos/Videos", assets = assets))
            _directoryGroup.addAll(directoryList)
        }
    }

    private suspend fun initAssets(requestType: RequestType) {
        assets.clear()
        assets.addAll(assetPickerRepository.getAssets(requestType))
    }

    fun getAssets(requestType: RequestType): List<AssetInfo> {
        val assetList = _directoryGroup.first { it.directory == directory }.assets

        return when (requestType) {
            RequestType.COMMON -> assetList
            RequestType.IMAGE -> assetList.filter(AssetInfo::isImage)
            RequestType.VIDEO -> assetList.filter(AssetInfo::isVideo)
        }
    }

    fun isFullSelected(): Boolean {
        return selectedList.size == assetPickerConfig.maxAssets
    }

    fun isSelected(assetInfo: AssetInfo): Boolean {
        return selectedList.any { it.id == assetInfo.id }
    }

    fun navigateToPreview(index: Int, requestType: RequestType) {
        navController.navigate("preview?index=$index&requestType=${requestType.name}")
    }
}