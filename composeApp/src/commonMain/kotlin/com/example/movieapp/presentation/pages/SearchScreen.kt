package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import org.koin.compose.viewmodel.koinViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.presentation.components.*
import com.example.movieapp.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    onMovieClick: (Int, Boolean) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- CUSTOM SEARCH HEADER ---
        Surface(
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
            modifier = Modifier.fillMaxWidth().statusBarsPadding(),
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Tên phim, diễn viên...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)) },
                    leadingIcon = { Icon(Icons.Rounded.Search, null, tint = MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(Icons.Rounded.Close, null)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    singleLine = true
                )
            }
        }

        if (searchQuery.isBlank()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // --- RECENT SEARCHES ---
                if (recentSearches.isNotEmpty()) {
                    item {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Tìm kiếm gần đây", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            TextButton(onClick = { viewModel.clearRecentSearches() }) {
                                Text("Xóa tất cả", fontSize = 12.sp)
                            }
                        }
                        FlowRow(
                            Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            recentSearches.forEach { query ->
                                AssistChip(
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.onSearchQueryChanged(query) 
                                    },
                                    label = { Text(query) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    border = null
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }

                // --- TRENDING SUGGESTIONS ---
                item {
                    SectionHeader("🎬 Đề xuất cho bạn", "Có thể bạn sẽ thích")
                }

                items(trendingMovies.chunked(2)) { pair ->
                    Row(
                        Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        pair.forEach { movie ->
                            Box(Modifier.weight(1f)) {
                                MovieGridCard(movie = movie, onMovieClick = onMovieClick)
                            }
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        } else {
            // --- SEARCH RESULTS GRID ---
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
                        loadState.refresh is LoadState.Loading -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                ShimmerGrid()
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                ErrorState(
                                    message = "Lỗi kết nối khi tìm kiếm",
                                    onRetry = { searchResults.retry() }
                                )
                            }
                        }
                        loadState.refresh is LoadState.NotLoading && itemCount == 0 -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                EmptyState(
                                    title = "Không tìm thấy phim",
                                    subtitle = "Thử nhập tên phim khác nhé!"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
