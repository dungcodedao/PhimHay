package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.movieapp.domain.model.ActorDetail
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActorUiState(
    val actor: ActorDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ActorViewModel @Inject constructor(
    private val repository: IMovieRepository,
    private val ssHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActorUiState())
    val uiState = _uiState.asStateFlow()

    private var actorId: Int = -1

    init {
        try {
            val route = ssHandle.toRoute<com.example.movieapp.presentation.navigation.ActorRoute>()
            actorId = route.actorId
            loadActorDetail(actorId)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Không tìm thấy thông tin diễn viên") }
        }
    }

    private fun loadActorDetail(id: Int) {
        viewModelScope.launch {
            repository.getActorDetail(id).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update { it.copy(actor = result.data, isLoading = false) }
                    is Resource.Error -> _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
            }
        }
    }

    fun retry() {
        if (actorId != -1) loadActorDetail(actorId)
    }
}
