package com.example.movieapp.core.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.example.movieapp.MovieApp

/**
 * Triển khai ColorUtil trên Android sử dụng thư viện Palette và Coil 3.
 */
actual object ColorUtil {
    actual suspend fun extractColorsFromImageUrl(imageUrl: String): Map<String, Color> {
        val context = MovieApp.instance.applicationContext
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        
        val result = (loader.execute(request) as? SuccessResult)?.image
        val bitmap = result?.toBitmap()
        
        return if (bitmap != null) {
            extractColorsFromBitmap(bitmap)
        } else {
            emptyMap()
        }
    }

    private fun extractColorsFromBitmap(bitmap: Bitmap): Map<String, Color> {
        val palette = Palette.from(bitmap).generate()
        val colors = mutableMapOf<String, Color>()
        
        palette.vibrantSwatch?.let { colors["vibrant"] = Color(it.rgb) }
        palette.darkVibrantSwatch?.let { colors["darkVibrant"] = Color(it.rgb) }
        palette.lightVibrantSwatch?.let { colors["lightVibrant"] = Color(it.rgb) }
        palette.mutedSwatch?.let { colors["muted"] = Color(it.rgb) }
        palette.darkMutedSwatch?.let { colors["darkMuted"] = Color(it.rgb) }
        
        return colors
    }
}
