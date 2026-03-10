package com.example.movieapp.core.util

import androidx.compose.ui.graphics.Color

/**
 * Triển khai ColorUtil trên iOS. 
 * Hiện tại trả về rỗng, sẽ được hoàn thiện bằng các API của iOS sau.
 */
actual object ColorUtil {
    actual suspend fun extractColorsFromImageUrl(imageUrl: String): Map<String, Color> {
        return emptyMap()
    }
}
