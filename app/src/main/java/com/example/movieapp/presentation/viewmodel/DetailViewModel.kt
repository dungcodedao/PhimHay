package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.movieapp.domain.model.*
import com.example.movieapp.domain.repository.IAuthRepository
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.repository.IRatingRepository
import com.example.movieapp.domain.usecase.GetMovieDetailUseCase
import com.example.movieapp.domain.usecase.ManageFavoriteUseCase
import com.example.movieapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.ui.graphics.Color
import com.example.movieapp.core.util.ColorUtil
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext



data class DetailUiState(
    val movieDetail: MovieDetail? = null,
    val videos: List<MovieVideo> = emptyList(),
    val cast: List<Cast> = emptyList(),
    val similarMovies: List<Movie> = emptyList(),
    val ratings: List<Rating> = emptyList(),
    val userRating: Rating? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isSubmittingRating: Boolean = false,
    val extractedColors: Map<String, Color> = emptyMap()
)


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val manageFavoriteUseCase: ManageFavoriteUseCase,
    private val movieRepository: IMovieRepository,
    private val ratingRepository: IRatingRepository,
    private val authRepository: IAuthRepository,
    private val ssHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    private var movieId: Int = -1

    init {
        try {
            val route = ssHandle.toRoute<com.example.movieapp.presentation.navigation.DetailRoute>()
            movieId = route.movieId
            loadAllData(movieId)
            observeFavoriteStatus(movieId)
            loadRatings(movieId)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Không tìm thấy thông tin phim") }
        }
    }

    private fun loadAllData(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Collect Movie Detail
            launch {
                getMovieDetailUseCase.getDetail(id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.update { it.copy(movieDetail = result.data, isLoading = false) }
                            // Add to History
                            movieRepository.addToHistory(result.data)
                            // Extract colors
                            extractColors(result.data.posterUrl)
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(error = result.message, isLoading = false) }
                        }
                        Resource.Loading -> { }
                    }
                }
            }

            // Collect Videos
            launch {
                getMovieDetailUseCase.getVideos(id).collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(videos = result.data) }
                    }
                }
            }

            // Collect Cast
            launch {
                getMovieDetailUseCase.getCast(id).collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(cast = result.data) }
                    }
                }
            }

            // Collect Similar Movies
            launch {
                getMovieDetailUseCase.getSimilar(id).collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(similarMovies = result.data) }
                    }
                }
            }
        }
    }

    private fun extractColors(imageUrl: String) {
        viewModelScope.launch {
            val colors = ColorUtil.extractColorsFromImageUrl(context, imageUrl)
            _uiState.update { it.copy(extractedColors = colors) }
        }
    }

    private fun loadRatings(id: Int) {
        viewModelScope.launch {
            ratingRepository.getMovieRatings(id).collect { result ->
                if (result is Resource.Success) {
                    _uiState.update { it.copy(ratings = result.data) }
                    
                    // Check if current user has rated
                    val userId = authRepository.getCurrentUserId()
                    if (userId != null) {
                        val userRating = result.data.find { it.userId == userId }
                        _uiState.update { it.copy(userRating = userRating) }
                    }
                }
            }
        }
    }

    fun submitRating(score: Int, comment: String) {
        val userId = authRepository.getCurrentUserId() ?: return
        val username = authRepository.getUsername() ?: "User"
        val avatar = authRepository.getAvatarUrl()
        
        val rating = Rating(
            userId = userId,
            userName = username,
            userAvatar = avatar,
            movieId = movieId,
            rating = score,
            comment = comment
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingRating = true) }
            val result = ratingRepository.submitRating(rating)
            if (result is Resource.Success) {
                loadRatings(movieId) // Refresh
            }
            _uiState.update { it.copy(isSubmittingRating = false) }
        }
    }

    private fun observeFavoriteStatus(id: Int) {
        viewModelScope.launch {
            manageFavoriteUseCase.isFavorite(id).collectLatest { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun toggleFavorite() {
        val movie = _uiState.value.movieDetail ?: return
        viewModelScope.launch {
            manageFavoriteUseCase.toggleFavorite(movie)
        }
    }

    fun retry() {
        if (movieId != -1) {
            loadAllData(movieId)
            loadRatings(movieId)
        }
    }
}
