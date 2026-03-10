package com.example.movieapp.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

/**
 * Triển khai PreferenceManager trên iOS sử dụng NSUserDefaults.
 */
actual class PreferenceManager actual constructor() {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    private val _isDarkMode = MutableStateFlow(
        if (userDefaults.objectForKey("is_dark_mode") != null) 
            userDefaults.boolForKey("is_dark_mode") 
        else true
    )
    actual val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _recentSearches = MutableStateFlow(getRecentSearchesFromPrefs())
    actual val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    actual fun setDarkMode(isDark: Boolean) {
        userDefaults.setBool(isDark, "is_dark_mode")
        _isDarkMode.value = isDark
    }

    actual fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        val current = _recentSearches.value.toMutableList()
        current.remove(query)
        current.add(0, query)
        val updated = current.take(10)
        userDefaults.setObject(updated.joinToString(","), "recent_searches")
        _recentSearches.value = updated
    }

    actual fun clearRecentSearches() {
        userDefaults.removeObjectForKey("recent_searches")
        _recentSearches.value = emptyList()
    }

    private fun getRecentSearchesFromPrefs(): List<String> {
        val saved = userDefaults.stringForKey("recent_searches") ?: ""
        return if (saved.isEmpty()) emptyList() else saved.split(",")
    }
}
