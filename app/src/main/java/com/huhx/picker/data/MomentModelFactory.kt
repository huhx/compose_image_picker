package com.huhx.picker.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MomentModelFactory(
    private val momentRepository: MomentRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MomentViewModel::class.java)) {
            MomentViewModel(momentRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel is Missing")
        }
    }
}