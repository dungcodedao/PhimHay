package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.data.util.NetworkConnectivityObserver
import com.example.movieapp.presentation.components.NetworkStatusBanner
import com.example.movieapp.presentation.viewmodel.ThemeViewModel
import com.example.movieapp.ui.theme.MovieAppTheme
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    
    private val networkObserver: NetworkConnectivityObserver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = koinViewModel()
            val isDarkTheme by themeViewModel.isDarkMode.collectAsStateWithLifecycle()

            MovieAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        NetworkStatusBanner(networkObserver)
                        Box(modifier = Modifier.weight(1f)) {
                            // Gọi hàm App dùng chung
                            App()
                        }
                    }
                }
            }
        }
    }
}
