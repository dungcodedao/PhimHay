package com.example.movieapp.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("movie_app_prefs", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(sharedPreferences.getBoolean("is_dark_mode", true))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    fun setDarkMode(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", isDark).apply()
        _isDarkMode.value = isDark
    }
}
