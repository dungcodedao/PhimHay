package com.example.movieapp.data.local

import androidx.room.*
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.movieapp.data.local.dao.*
import com.example.movieapp.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Room Database - Single Source of Truth của ứng dụng.
 * Hỗ trợ Kotlin Multiplatform (Android & iOS).
 */
@Database(
    entities = [
        MovieEntity::class, 
        FavoriteMovieEntity::class, 
        HistoryMovieEntity::class,
        MovieDetailEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
}

/**
 * Tạo instance của MovieDatabase với các driver tương ứng theo nền tảng.
 */
fun getRoomDatabase(
    builder: RoomDatabase.Builder<MovieDatabase>
): MovieDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase>
