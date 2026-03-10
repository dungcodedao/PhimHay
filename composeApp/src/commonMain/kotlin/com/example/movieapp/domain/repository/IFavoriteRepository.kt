package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Quản lý danh sách phim yêu thích, kết hợp Local Room và Remote Supabase.
 */
interface IFavoriteRepository {
    fun getFavorites(): Flow<List<Movie>>
    fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun toggleFavorite(movie: MovieDetail)
    suspend fun syncFromCloud(): Flow<Resource<Unit>>
    suspend fun clearAll()
}
