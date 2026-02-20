package com.example.movieapp.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

object ColorUtil {
    suspend fun extractColorsFromImageUrl(context: Context, imageUrl: String): Map<String, Color> {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Required for Palette
            .build()
        
        val result = (loader.execute(request) as? SuccessResult)?.drawable
        val bitmap = (result as? BitmapDrawable)?.bitmap
        
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
