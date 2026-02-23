package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.presentation.components.*
import com.example.movieapp.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int, Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagingMovies = viewModel.pagingMovies.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Header
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HeaderSection(
                        uiState = uiState,
                        onGenreClick = { viewModel.onGenreSelected(it) }
                    )
                }

                // ========== LOADING STATE ==========
                if (uiState.isLoading && uiState.trendingMovies.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            ShimmerCarousel()
                            Spacer(Modifier.height(24.dp))
                            ShimmerGrid()
                        }
                    }
                }

                // ========== TRENDING CAROUSEL ==========
                if (uiState.trendingMovies.isNotEmpty() && uiState.selectedGenreId == null) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            SectionHeader("🔥 Xu hướng", "Thịnh hành trong tuần")
                            Spacer(Modifier.height(8.dp))

                            val pagerState =
                                androidx.compose.foundation.pager.rememberPagerState { uiState.trendingMovies.size }

                            androidx.compose.foundation.pager.HorizontalPager(
                                state = pagerState,
                                contentPadding = PaddingValues(horizontal = 32.dp),
                                pageSpacing = 16.dp,
                                modifier = Modifier.fillMaxWidth().height(200.dp)
                            ) { page ->
                                val movie = uiState.trendingMovies[page]
                                TrendingMovieCard(
                                    movie = movie,
                                    onMovieClick = { onMovieClick(it, movie.isTV) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }

                if (uiState.selectedGenreId == null) {
                    // ========== PHIM BỘ (TV SERIES) ==========
                    if (uiState.trendingTV.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            MovieHorizontalSection(
                                title = "📺 Phim bộ Hot",
                                subtitle = "Series truyền hình phổ biến",
                                movies = uiState.trendingTV,
                                onMovieClick = onMovieClick
                            )
                        }
                    }

                    // ========== PHIM LẺ PHỔ BIẾN ==========
                    if (uiState.popularMovies.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            MovieHorizontalSection(
                                title = "🍿 Phim lẻ Phổ biến",
                                subtitle = "Được xem nhiều nhất",
                                movies = uiState.popularMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }

                    // ========== SẮP CHIẾU ==========
                    if (uiState.upcomingMovies.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            MovieHorizontalSection(
                                title = "🔔 Sắp chiếu",
                                subtitle = "Đừng bỏ lỡ các siêu phẩm mới",
                                movies = uiState.upcomingMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }

                    // ========== ĐÁNH GIÁ CAO ==========
                    if (uiState.topRatedMovies.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            MovieHorizontalSection(
                                title = "🏆 Đánh giá cao",
                                subtitle = "Phim hay nhất mọi thời đại",
                                movies = uiState.topRatedMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }
                }

                // ========== EXPLORE TITLE ==========
                item(span = { GridItemSpan(maxLineSpan) }) {
                    val sectionTitle = if (uiState.selectedGenreId == null) "⭐ Khám phá thêm" else {
                        val genreName =
                            uiState.genres.find { it.id == uiState.selectedGenreId }?.name ?: ""
                        "🎞 Phim $genreName"
                    }
                    SectionHeader(sectionTitle)
                }

                // ========== PAGING LIST (POPULAR) ==========
                items(pagingMovies.itemCount) { index ->
                    val movie = pagingMovies[index]
                    if (movie != null) {
                        Box(
                            modifier = Modifier.padding(
                                start = if (index % 2 == 0) 16.dp else 4.dp,
                                end = if (index % 2 == 0) 4.dp else 16.dp
                            )
                        ) {
                            MovieGridCard(
                                movie = movie,
                                onMovieClick = { onMovieClick(it, movie.isTV) }
                            )
                        }
                    }
                }

                // ========== PAGING STATES ==========
                pagingMovies.apply {
                    when {
                        loadState.refresh is LoadState.Loading && !uiState.isLoading -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                ShimmerGrid()
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp)
                                            .align(androidx.compose.ui.Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieHorizontalSection(
    title: String,
    subtitle: String,
    movies: List<com.example.movieapp.domain.model.Movie>,
    onMovieClick: (Int, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        SectionHeader(title, subtitle)
        Spacer(Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies, key = { "${title}_${it.id}" }) { movie ->
                MovieGridCard(
                    movie = movie,
                    onMovieClick = { onMovieClick(it, movie.isTV) },
                    modifier = Modifier.width(150.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun HeaderSection(
    uiState: com.example.movieapp.presentation.viewmodel.HomeUiState,
    onGenreClick: (Int?) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Chào ngày mới! 👋",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "🎬 PhimHay",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            item {
                GenreChip(
                    "Tất cả",
                    isSelected = uiState.selectedGenreId == null,
                    onClick = { onGenreClick(null) })
            }
            items(uiState.genres) { genre ->
                GenreChip(
                    genre.name,
                    isSelected = uiState.selectedGenreId == genre.id,
                    onClick = { onGenreClick(genre.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        shape = RoundedCornerShape(100.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = null
    )
}
