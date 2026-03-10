package com.example.movieapp.data.local.dao

import androidx.room.*
import com.example.movieapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO xử lý việc cache dữ liệu phim từ API để hiển thị offline.
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM cached_movies WHERE category = :category ORDER BY cachedAt DESC")
    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM cached_movies WHERE category = :category ORDER BY cachedAt DESC")
    suspend fun getMoviesByCategoryOnce(category: String): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM cached_movies WHERE category = :category")
    suspend fun clearMoviesByCategory(category: String)

    // --- Caching cho Chi tiết Phim ---
    @Query("SELECT * FROM cached_movie_details WHERE id = :movieId")
    suspend fun getMovieDetailById(movieId: Int): com.example.movieapp.data.local.entity.MovieDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(detail: com.example.movieapp.data.local.entity.MovieDetailEntity)
}
