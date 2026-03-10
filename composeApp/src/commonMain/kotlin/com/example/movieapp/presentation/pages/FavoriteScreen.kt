package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.example.movieapp.presentation.components.EmptyState
import com.example.movieapp.presentation.components.MovieGridCard
import com.example.movieapp.presentation.components.ShimmerGrid
import com.example.movieapp.presentation.viewmodel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onMovieClick: (Int, Boolean) -> Unit,
    viewModel: FavoriteViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        Text(
            "Phim yêu thích",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.weight(1f)
        ) {
            if (uiState.isLoading && uiState.favorites.isEmpty()) {
                ShimmerGrid()
            } else if (uiState.favorites.isEmpty()) {
                EmptyState(
                    title = "Chưa có phim yêu thích",
                    subtitle = "Nhấn vào biểu tượng ❤️ trên màn hình phim để thêm vào đây nhé!"
                )
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
}
