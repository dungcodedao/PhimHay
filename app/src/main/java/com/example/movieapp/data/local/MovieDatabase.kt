package com.example.movieapp.data.local

import androidx.room.*
import com.example.movieapp.data.local.dao.*
import com.example.movieapp.data.local.entity.*

/**
 * Room Database - Single Source of Truth của ứng dụng.
 */
@Database(
    entities = [
        MovieEntity::class, 
        FavoriteMovieEntity::class, 
        HistoryMovieEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
}
