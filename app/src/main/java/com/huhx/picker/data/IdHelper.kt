package com.huhx.picker.data

object IdHelper {
    private var id: Int = moments.size

    @Synchronized
    fun nextID(): Int = id++
}