package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.usecase.ManageFavoriteUseCase
import com.example.movieapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteUiState(
    val favorites: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val manageFavoriteUseCase: ManageFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorites()
        syncWithCloud()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            manageFavoriteUseCase.getAllFavorites().collect { list ->
                _uiState.update { it.copy(favorites = list) }
            }
        }
    }

    private fun syncWithCloud() {
        viewModelScope.launch {
            manageFavoriteUseCase.syncWithCloud().collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update { it.copy(isLoading = false) }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }
}
