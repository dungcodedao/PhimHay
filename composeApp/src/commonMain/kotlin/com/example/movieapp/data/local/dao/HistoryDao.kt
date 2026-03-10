package com.example.movieapp.data.local.dao

import androidx.room.*
import com.example.movieapp.data.local.entity.HistoryMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO xử lý lịch sử xem phim của người dùng tại local.
 */
@Dao
interface HistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY watchedAt DESC LIMIT 50")
    fun getWatchHistory(): Flow<List<HistoryMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(movie: HistoryMovieEntity)

    @Query("DELETE FROM watch_history WHERE id = :movieId")
    suspend fun removeFromHistory(movieId: Int)

    @Query("DELETE FROM watch_history")
    suspend fun clearHistory()
}
