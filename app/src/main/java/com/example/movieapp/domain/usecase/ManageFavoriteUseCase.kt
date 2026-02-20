package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail
import com.example.movieapp.domain.repository.IFavoriteRepository
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase quản lý toàn bộ logic về phim yêu thích và đồng bộ Cloud.
 */
class ManageFavoriteUseCase @Inject constructor(
    private val repository: IFavoriteRepository
) {
    fun getAllFavorites(): Flow<List<Movie>> = repository.getFavorites()
    
    fun isFavorite(movieId: Int): Flow<Boolean> = repository.isFavorite(movieId)
    
    suspend fun toggleFavorite(movie: MovieDetail) = repository.toggleFavorite(movie)
    
    suspend fun syncWithCloud(): Flow<Resource<Unit>> = repository.syncFromCloud()
}
