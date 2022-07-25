package com.huhx.picker.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huhx.picker.constant.AssetPickerConfig

class AssetViewModelFactory(
    private val assetPickerConfig: AssetPickerConfig
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AssetViewModel::class.java)) {
            AssetViewModel(this.assetPickerConfig) as T
        } else {
            throw IllegalArgumentException("ViewModel is Missing")
        }
    }
}