package com.huhx.picker.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.huhx.picker.AssetRoute
import com.huhx.picker.model.AssetDirectory
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.RequestType
import com.huhx.picker.provider.AssetPickerRepository
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

        return assetList.filter {
            when (requestType) {
                RequestType.COMMON -> true
                RequestType.IMAGE -> it.isImage()
                RequestType.VIDEO -> it.isVideo()
            }
        }
    }

    fun navigateToPreview(index: Int, requestType: RequestType) {
        navController.navigate(AssetRoute.preview(index, requestType))
    }

    fun deleteImage(cameraUri: Uri?) {
        assetPickerRepository.deleteByUri(cameraUri)
    }

    fun getUri(): Uri? {
        return assetPickerRepository.insertImage()
    }
}