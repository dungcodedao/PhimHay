package com.example.movieapp.data.local.dao

import androidx.room.*
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO xử lý danh sách phim yêu thích lưu tại local, đồng bộ với Supabase.
 */
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_movies WHERE userId = :userId ORDER BY addedAt DESC")
    fun getFavorites(userId: String): Flow<List<FavoriteMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId AND userId = :userId")
    suspend fun removeFavorite(movieId: Int, userId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId AND userId = :userId)")
    fun isFavorite(movieId: Int, userId: String): Flow<Boolean>

    @Query("DELETE FROM favorite_movies WHERE userId = :userId")
    suspend fun clearAll(userId: String)
}
