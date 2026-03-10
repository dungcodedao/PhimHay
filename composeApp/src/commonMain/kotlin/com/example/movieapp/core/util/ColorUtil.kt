package com.example.movieapp.core.util

import androidx.compose.ui.graphics.Color

/**
 * Tiện ích trích xuất màu sắc từ hình ảnh.
 * Sử dụng cơ chế expect/actual của KMP để triển khai riêng cho Android và iOS.
 */
expect object ColorUtil {
    suspend fun extractColorsFromImageUrl(imageUrl: String): Map<String, Color>
}
