package com.huhx.picker.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MomentViewModel constructor(repository: MomentRepository) : ViewModel() {

    val selectedList = mutableStateListOf<AssetInfo>()
    var content by mutableStateOf("")

    private val _moments = mutableStateListOf<Moment>()
    val moments: List<Moment>
        get() = _moments

    fun addMoment(moment: Moment) {
        _moments.add(0, moment)
    }

    init {
        _moments.clear()
        _moments.addAll(repository.queryMoments())
    }
}