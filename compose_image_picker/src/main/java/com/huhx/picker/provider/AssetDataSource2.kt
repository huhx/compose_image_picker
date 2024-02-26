package com.huhx.picker.provider

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.RequestType
import java.io.IOException

class AssetDataSource2(
    private val assetPickerRepository: AssetPickerRepository,
    private val requestType: RequestType
) : PagingSource<Int, AssetInfo>() {

    override fun getRefreshKey(state: PagingState<Int, AssetInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AssetInfo> {
        return try {
            val nextPage = params.key ?: 0
            val momentResult = assetPickerRepository.getAssets(requestType, nextPage)
            LoadResult.Page(
                data = momentResult,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (momentResult.isEmpty()) null else nextPage + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }
}