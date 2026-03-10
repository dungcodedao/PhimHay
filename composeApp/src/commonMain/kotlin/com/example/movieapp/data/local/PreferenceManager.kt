package com.example.movieapp.data.local

import kotlinx.coroutines.flow.StateFlow

/**
 * Quản lý các tùy chọn người dùng (cài đặt app, lịch sử tìm kiếm).
 * Sử dụng expect/actual để hỗ trợ SharedPreferences trên Android và NSUserDefaults trên iOS.
 */
expect class PreferenceManager() {
    val isDarkMode: StateFlow<Boolean>
    val recentSearches: StateFlow<List<String>>

    fun setDarkMode(isDark: Boolean)
    fun addRecentSearch(query: String)
    fun clearRecentSearches()
}
