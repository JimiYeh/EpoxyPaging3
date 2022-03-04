package com.example.epoxypaging3.repository.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.epoxypaging3.model.SearchUserResponse
import com.example.epoxypaging3.network.ApiResponse
import com.example.epoxypaging3.network.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchUsersPagingSource(
  private val searchService: SearchService,
  private val query: String
) : PagingSource<Int, SearchUserResponse.Item>() {
  override fun getRefreshKey(state: PagingState<Int, SearchUserResponse.Item>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchUserResponse.Item> {
    val pageNumber = params.key ?: 1
    val response = withContext(Dispatchers.IO) {
      searchService.getSearchUsers(
        query = query,
        page = pageNumber
      )
    }

    return when (response) {
      is ApiResponse.ApiSuccess ->
        LoadResult.Page(
          data = response.data.items,
          prevKey = null, // Only paging forward.
          nextKey = if (response.data.incompleteResults) null else pageNumber + 1
        )

      is ApiResponse.ApiFail ->
        LoadResult.Error(Throwable("api fail http code ${response.code}"))

      is ApiResponse.UnknownError ->
        LoadResult.Error(response.error ?: Throwable("Unknown Error"))
    }
  }
}