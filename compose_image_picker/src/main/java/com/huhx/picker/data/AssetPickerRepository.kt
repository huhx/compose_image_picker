package com.huhx.picker.data

import android.content.Context
import com.huhx.picker.constant.RequestType

class AssetPickerRepository(
    private val context: Context
) {
    suspend fun getAssets(requestType: RequestType): List<AssetInfo> {
        return AssetLoader.load(context, requestType)
    }
}