package com.example.movieapp

import androidx.compose.runtime.Composable
import com.example.movieapp.presentation.navigation.AppNavigation
import com.example.movieapp.ui.theme.MovieAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        MovieAppTheme {
            AppNavigation()
        }
    }
}
