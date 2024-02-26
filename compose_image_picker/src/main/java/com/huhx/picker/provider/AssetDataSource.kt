package com.huhx.picker.provider

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.huhx.picker.model.AssetInfo

private const val Zero = 0
private const val One = 1

class AssetDataSource(private val onFetch: (limit: Int, offset: Int) -> List<AssetInfo>) :
    PagingSource<Int, AssetInfo>() {

    override fun getRefreshKey(state: PagingState<Int, AssetInfo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(One)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(One)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AssetInfo> {
        val pageNumber = params.key ?: Zero
        val pageSize = params.loadSize
        val pictures = onFetch.invoke(pageSize, pageNumber * pageSize)
        val prevKey = if (pageNumber > Zero) pageNumber.minus(One) else null
        val nextKey = if (pictures.isNotEmpty()) pageNumber.plus(One) else null

        return LoadResult.Page(
            data = pictures,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}