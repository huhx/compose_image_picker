package com.huhx.picker.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.huhx.picker.AssetRoute
import com.huhx.picker.model.AssetDirectory
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.RequestType
import com.huhx.picker.provider.AssetPickerRepository

const val init_directory = "Photos/Videos"

internal class AssetViewModel(
    private val assetPickerRepository: AssetPickerRepository,
    private val navController: NavController,
) : ViewModel() {

    private val assets = mutableStateListOf<AssetInfo>()
    private val _directoryGroup = mutableStateListOf<AssetDirectory>()

    val directoryGroup: List<AssetDirectory>
        get() = _directoryGroup

    val selectedList = mutableStateListOf<AssetInfo>()
    var directory by mutableStateOf(init_directory)

    suspend fun initDirectories() {
        initAssets(RequestType.COMMON)
        val directoryList = assets.groupBy {
            it.directory
        }.map {
            AssetDirectory(directory = it.key, assets = it.value)
        }
        _directoryGroup.clear()
        _directoryGroup.add(AssetDirectory(directory = init_directory, assets = assets))
        _directoryGroup.addAll(directoryList)
    }

    private suspend fun initAssets(requestType: RequestType) {
        assets.clear()
        assets.addAll(assetPickerRepository.getAssets(requestType))
    }

    fun clear() {
        selectedList.clear()
    }

    fun toggleSelect(selected: Boolean, assetInfo: AssetInfo) {
        if (selected) {
            select(assetInfo)
        } else {
            unSelect(assetInfo)
        }
    }

    private fun select(assetInfo: AssetInfo) {
        selectedList += assetInfo
    }

    private fun unSelect(assetInfo: AssetInfo) {
        selectedList -= assetInfo
    }

    fun getGroupedAssets(requestType: RequestType): Map<String, List<AssetInfo>> {
        val assetList = _directoryGroup.first { it.directory == directory }.assets

        return assetList.filter {
            when (requestType) {
                RequestType.COMMON -> true
                RequestType.IMAGE -> it.isImage()
                RequestType.VIDEO -> it.isVideo()
            }
        }
            .sortedByDescending { it.date }
            .groupBy { it.dateString }
    }

    fun isAllSelected(assets: List<AssetInfo>): Boolean {
        val selectedIds = selectedList.map { it.id }
        val ids = assets.map { it.id }
        return selectedIds.containsAll(ids)
    }

    fun hasSelected(assets: List<AssetInfo>): Boolean {
        val selectedIds = selectedList.map { it.id }
        val ids = assets.map { it.id }

        return selectedIds.any { ids.contains(it) }
    }

    fun navigateToPreview(index: Int, dateString: String, requestType: RequestType) {
        navController.navigate(AssetRoute.preview(index, dateString, requestType))
    }

    fun deleteImage(cameraUri: Uri?) {
        assetPickerRepository.deleteByUri(cameraUri)
    }

    fun getUri(): Uri? {
        return assetPickerRepository.insertImage()
    }

    fun unSelectAll(resources: List<AssetInfo>) {
        selectedList -= resources.toSet()
    }

    fun selectAll(resources: List<AssetInfo>, maxAssets: Int): Boolean {
        val selectedIds = selectedList.map { it.id }
        val newSelectedList = resources.filterNot { selectedIds.contains(it.id) }

        selectedList += newSelectedList.subList(0, minOf(maxAssets - selectedIds.size, newSelectedList.size))
        return maxAssets - selectedIds.size < newSelectedList.size
    }
}