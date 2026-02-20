package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.presentation.components.MovieGridCard
import com.example.movieapp.presentation.components.ShimmerGrid
import com.example.movieapp.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Tìm kiếm phim...") },
            leadingIcon = { Icon(Icons.Rounded.Search, null) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(searchResults.itemCount) { index ->
                val movie = searchResults[index]
                if (movie != null) {
                    MovieGridCard(movie = movie, onMovieClick = onMovieClick)
                }
            }

            searchResults.apply {
                when {
                    loadState.refresh is LoadState.Loading && searchQuery.isNotBlank() -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            ShimmerGrid()
                        }
                    }
                    loadState.refresh is LoadState.NotLoading && itemCount == 0 && searchQuery.isNotBlank() -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text("Không tìm thấy kết quả cho \"$searchQuery\"", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}
