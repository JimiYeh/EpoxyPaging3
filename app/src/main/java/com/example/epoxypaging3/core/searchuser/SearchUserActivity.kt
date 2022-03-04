package com.example.epoxypaging3.core.searchuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.example.epoxypaging3.R
import com.example.epoxypaging3.databinding.ActivitySearchBinding
import com.example.epoxypaging3.network.getSearchService
import com.example.epoxypaging3.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class SearchUserActivity : AppCompatActivity() {

  private val viewModel: SearchUserViewModel by viewModels {
    object : ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchUserViewModel(getSearchService()) as T
      }
    }
  }

  private val binding: ActivitySearchBinding by viewBinding(ActivitySearchBinding::inflate)

  private var searchJob: Job? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    val searchTextFlow = MutableStateFlow("")


    binding.etSearch.apply {
      doOnTextChanged { text, _, _, _ ->
        lifecycleScope.launch {
          searchTextFlow.emit(text.toString())
        }
      }
    }

    lifecycleScope.launch {
      searchTextFlow.debounce(1000).collectLatest { query ->
        searchJob?.cancel()
        searchJob = launch {
          viewModel.searchUser(query)
          val adapter = UserAdapter()
          binding.ervUsers.setController(adapter)
          viewModel.userListFlow?.collect {
            adapter.submitData(it)
          }
        }
      }
    }
  }
}