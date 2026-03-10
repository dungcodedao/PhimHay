package com.example.movieapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.movieapp.MovieApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Triển khai PreferenceManager trên Android sử dụng SharedPreferences.
 */
actual class PreferenceManager actual constructor() {
    private val sharedPreferences: SharedPreferences =
        MovieApp.instance.applicationContext.getSharedPreferences("movie_app_prefs", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(sharedPreferences.getBoolean("is_dark_mode", true))
    actual val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _recentSearches = MutableStateFlow(getRecentSearchesFromPrefs())
    actual val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    actual fun setDarkMode(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", isDark).apply()
        _isDarkMode.value = isDark
    }

    actual fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        val current = _recentSearches.value.toMutableList()
        current.remove(query)
        current.add(0, query)
        val updated = current.take(10)
        sharedPreferences.edit().putString("recent_searches", updated.joinToString(",")).apply()
        _recentSearches.value = updated
    }

    actual fun clearRecentSearches() {
        sharedPreferences.edit().remove("recent_searches").apply()
        _recentSearches.value = emptyList()
    }

    private fun getRecentSearchesFromPrefs(): List<String> {
        val saved = sharedPreferences.getString("recent_searches", "") ?: ""
        return if (saved.isEmpty()) emptyList() else saved.split(",")
    }
}
