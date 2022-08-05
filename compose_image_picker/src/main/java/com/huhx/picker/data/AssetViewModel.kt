package com.huhx.picker.data

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.huhx.picker.constant.RequestType
import kotlinx.coroutines.launch

internal class AssetViewModel(
    private val assetPickerRepository: AssetPickerRepository,
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

    fun navigateToPreview(index: Int, requestType: RequestType) {
        navController.navigate("asset_preview?index=$index&requestType=${requestType.name}")
    }

    fun deleteImage(cameraUri: Uri?) {
        assetPickerRepository.deleteByUri(cameraUri)
    }

    fun getUri(): Uri? {
        return assetPickerRepository.insertImage()
    }
}