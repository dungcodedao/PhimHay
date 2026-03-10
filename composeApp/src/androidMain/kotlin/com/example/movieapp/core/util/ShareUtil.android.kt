package com.example.movieapp.core.util

import android.content.Intent
import com.example.movieapp.MovieApp

/**
 * Triển khai ShareUtil cho Android.
 */
actual object ShareUtil {
    actual fun shareText(text: String, title: String) {
        val context = MovieApp.instance
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
