package com.example.movieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.local.PreferenceManager

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn



class ThemeViewModel (
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    
    val isDarkMode = preferenceManager.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun toggleTheme() {
        preferenceManager.setDarkMode(!isDarkMode.value)
    }
}
