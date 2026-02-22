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
import com.example.movieapp.presentation.components.EmptyState
import com.example.movieapp.presentation.components.ErrorState
import com.example.movieapp.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
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

        if (searchQuery.isBlank()) {
            EmptyState(
                title = "Tìm phim bạn yêu thích",
                subtitle = "Nhập tên bộ phim, diễn viên hoặc từ khóa để bắt đầu khám phá."
            )
        } else {
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
                                    title = "Không tìm thấy kết quả",
                                    subtitle = "Chúng mình không tìm thấy phim nào khớp với \"$searchQuery\". Thử từ khóa khác xem sao!"
                                )
                            }
                        }
                        loadState.append is LoadState.Loading -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                }
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Button(onClick = { searchResults.retry() }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    Text("Thử lại")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
