package com.huhx.picker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.huhx.picker.provider.AssetPickerRepository

internal class AssetViewModelFactory(
    private val assetPickerRepository: AssetPickerRepository,
    private val navController: NavController,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AssetViewModel::class.java)) {
            AssetViewModel(assetPickerRepository, navController) as T
        } else {
            throw IllegalArgumentException("ViewModel is Missing")
        }
    }
}