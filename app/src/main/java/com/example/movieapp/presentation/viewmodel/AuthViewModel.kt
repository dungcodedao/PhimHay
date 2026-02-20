package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.IAuthRepository
import com.example.movieapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userEmail: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.isUserLoggedIn().collect { loggedIn ->
                _uiState.update { 
                    it.copy(
                        isLoggedIn = loggedIn,
                        userEmail = if (loggedIn) authRepository.getCurrentUserEmail() else null,
                        username = if (loggedIn) authRepository.getUsername() else null,
                        avatarUrl = if (loggedIn) authRepository.getAvatarUrl() else maritalNull()
                    ) 
                }
            }
        }
    }

    private fun maritalNull(): String? = null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                handleAuthResult(result)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(email, password).collect { result ->
                handleAuthResult(result)
            }
        }
    }

    private fun handleAuthResult(result: Resource<Unit>) {
        when (result) {
            is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            is Resource.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
        }
    }

    fun updateProfile(username: String?, avatarUrl: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.updateProfile(username, avatarUrl)
            if (result is Resource.Success) {
                _uiState.update { it.copy(isLoading = false, username = username ?: it.username, avatarUrl = avatarUrl ?: it.avatarUrl) }
            } else if (result is Resource.Error) {
                _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
        }
    }

    fun uploadAvatar(byteArray: ByteArray) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val fileName = "avatar_${System.currentTimeMillis()}.jpg"
            val result = authRepository.uploadAvatar(byteArray, fileName)
            when (result) {
                is Resource.Success -> {
                    updateProfile(null, result.data)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun clearState() {
        _uiState.update { it.copy(error = null, isSuccess = false, isLoading = false) }
    }
}
