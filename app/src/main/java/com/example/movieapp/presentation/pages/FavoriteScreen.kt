package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.presentation.components.MovieGridCard
import com.example.movieapp.presentation.components.ShimmerGrid
import com.example.movieapp.presentation.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Text(
            "Phim yêu thích",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (uiState.isLoading) {
            ShimmerGrid()
        } else if (uiState.favorites.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có phim yêu thích nào", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.favorites) { movie ->
                    MovieGridCard(movie = movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}
