package com.example.epoxypaging3.core.searchuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.epoxypaging3.model.SearchUserResponse
import com.example.epoxypaging3.network.SearchService
import com.example.epoxypaging3.repository.search.SearchUsersPagingSource
import kotlinx.coroutines.flow.Flow

class SearchUserViewModel(private val searchService: SearchService) : ViewModel() {

  var userListFlow: Flow<PagingData<SearchUserResponse.Item>>? = null

  fun searchUser(query: String) {
    userListFlow = if (query.isEmpty())
      null
    else
      Pager(PagingConfig(pageSize = 30)) {
        SearchUsersPagingSource(searchService, query)
      }.flow
        .cachedIn(viewModelScope)
  }
}