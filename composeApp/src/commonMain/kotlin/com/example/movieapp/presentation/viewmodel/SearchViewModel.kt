package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.usecase.SearchMoviesUseCase
import com.example.movieapp.domain.usecase.GetHomeMoviesUseCase
import com.example.movieapp.data.local.PreferenceManager

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



class SearchViewModel (
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getHomeMoviesUseCase: GetHomeMoviesUseCase,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies = _trendingMovies.asStateFlow()

    val recentSearches = preferenceManager.recentSearches

    init {
        loadTrendingMovies()
    }

    private fun loadTrendingMovies() {
        viewModelScope.launch {
            getHomeMoviesUseCase.getTrending().collect { resource ->
                if (resource is com.example.movieapp.domain.util.Resource.Success) {
                    _trendingMovies.value = resource.data ?: emptyList()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<PagingData<Movie>> = _searchQuery
        .debounce(500)
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            // Lưu vào lịch sử khi có kết quả tìm kiếm (hoặc ít nhất là khi user nhập xong)
            preferenceManager.addRecentSearch(query)
            searchMoviesUseCase.getPaged(query)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearRecentSearches() {
        preferenceManager.clearRecentSearches()
    }
}
