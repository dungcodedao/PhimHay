package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.domain.model.Genre
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.usecase.GetHomeMoviesUseCase
import com.example.movieapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val trendingTV: List<Movie> = emptyList(),
    val popularTV: List<Movie> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeMoviesUseCase: GetHomeMoviesUseCase,
    private val movieRepository: IMovieRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val selectedGenreFlow = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingMovies: Flow<PagingData<Movie>> = selectedGenreFlow.flatMapLatest { genreId ->
        if (genreId == null) {
            getHomeMoviesUseCase.getPopularPaging()
        } else {
            getHomeMoviesUseCase.getByGenrePaging(genreId)
        }
    }.cachedIn(viewModelScope)

    init {
        loadGenres()
        loadAllContent()
    }

    private fun loadGenres() {
        viewModelScope.launch {
            movieRepository.getMovieGenres().collect { result ->
                if (result is Resource.Success) {
                    _uiState.update { it.copy(genres = result.data) }
                }
            }
        }
    }

    private fun loadAllContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Trending
            launch {
                getHomeMoviesUseCase.getTrending().collect { result ->
                    if (result is Resource.Success) _uiState.update { it.copy(trendingMovies = result.data) }
                }
            }

            // Popular (Phim lẻ)
            launch {
                getHomeMoviesUseCase.getPopular().collect { result ->
                    if (result is Resource.Success) _uiState.update { it.copy(popularMovies = result.data) }
                }
            }

            // Phim bộ (TV Trending)
            launch {
                getHomeMoviesUseCase.getTrendingTV().collect { result ->
                    if (result is Resource.Success) _uiState.update { it.copy(trendingTV = result.data) }
                }
            }

            // TV Popular
            launch {
                getHomeMoviesUseCase.getPopularTV().collect { result ->
                    if (result is Resource.Success) _uiState.update { it.copy(popularTV = result.data) }
                }
            }

            // Sắp chiếu
            launch {
                getHomeMoviesUseCase.getUpcoming().collect { result ->
                    if (result is Resource.Success) _uiState.update { it.copy(upcomingMovies = result.data) }
                }
            }

            // Đánh giá cao
            launch {
                getHomeMoviesUseCase.getTopRated().collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(topRatedMovies = result.data, isLoading = false) }
                    }
                }
            }
        }
    }

    fun onGenreSelected(genreId: Int?) {
        _uiState.update { it.copy(selectedGenreId = genreId) }
        selectedGenreFlow.value = genreId
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true, error = null) }
        loadGenres()
        loadAllContent()
        _uiState.update { it.copy(isRefreshing = false) }
    }
}
