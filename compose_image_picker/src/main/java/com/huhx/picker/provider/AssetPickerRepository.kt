package com.huhx.picker.provider

import android.content.Context
import android.net.Uri
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.RequestType

private const val Zero = 0
private const val One = 1

class AssetPickerRepository(private val context: Context) {
    fun getAssets(requestType: RequestType, pageNumber: Int): List<AssetInfo> {
        val limit = 160
        val offset = pageNumber * limit
        return AssetLoader.load(context, requestType, limit, offset)
    }

    suspend fun getCount(): Int {
        val cursor = AssetLoader.createCursor(context, Int.MAX_VALUE, Zero) ?: return Zero
        val count = cursor.count
        cursor.close()
        return count
    }

    fun insertImage(): Uri? {
        return AssetLoader.insertImage(context)
    }

    fun findByUri(uri: Uri?): AssetInfo? {
        return uri?.let { AssetLoader.findByUri(context, it) }
    }

    fun deleteByUri(uri: Uri?) {
        uri?.let { AssetLoader.deleteByUri(context, it) }
    }
}