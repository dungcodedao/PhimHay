package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.IMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val history: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: IMovieRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getWatchHistory().collect { list ->
                _uiState.update { it.copy(history = list, isLoading = false) }
            }
        }
    }

    fun removeFromHistory(movieId: Int) {
        viewModelScope.launch {
            repository.removeFromHistory(movieId)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
