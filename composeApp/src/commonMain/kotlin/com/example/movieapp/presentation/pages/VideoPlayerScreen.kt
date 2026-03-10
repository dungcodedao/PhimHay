package com.example.movieapp.presentation.pages

import androidx.compose.runtime.Composable

/**
 * Màn hình trình phát video.
 * Sử dụng expect/actual để tận dụng các thư viện Native Player của từng nền tảng.
 */
@Composable
expect fun VideoPlayerScreen(
    url: String,
    title: String,
    onBackClick: () -> Unit
)
