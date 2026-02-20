package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
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
    val freeMovies: List<Movie> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
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
        loadTrendingAndFree()
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

    private fun loadTrendingAndFree() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Collect Trending
            launch {
                getHomeMoviesUseCase.getTrending().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.update { it.copy(trendingMovies = result.data, isLoading = false) }
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(error = result.message, isLoading = false) }
                        }
                        Resource.Loading -> { }
                    }
                }
            }

            // Collect Free Phim (Popular)
            launch {
                getHomeMoviesUseCase.getPopular().collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(freeMovies = result.data.take(15)) }
                    }
                }
            }
        }
    }

    fun onGenreSelected(genreId: Int?) {
        _uiState.update { it.copy(selectedGenreId = genreId) }
        selectedGenreFlow.value = genreId
    }
}
