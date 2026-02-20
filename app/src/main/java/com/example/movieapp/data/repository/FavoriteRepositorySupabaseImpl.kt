package com.example.movieapp.data.repository

import com.example.movieapp.data.local.dao.FavoriteDao
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.data.mapper.toFavoriteEntity
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetail
import com.example.movieapp.domain.repository.IFavoriteRepository
import com.example.movieapp.domain.util.Resource
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Quản lý danh sách yêu thích bằng Supabase và Room.
 * Đồng bộ hai chiều giữa Local và Cloud.
 */
@Singleton
class FavoriteRepositorySupabaseImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val auth: Auth,
    private val postgrest: Postgrest
) : IFavoriteRepository {

    private val currentUserId: String?
        get() = auth.currentUserOrNull()?.id

    override fun getFavorites(): Flow<List<Movie>> {
        val userId = currentUserId ?: "local"
        return favoriteDao.getFavorites(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        val userId = currentUserId ?: "local"
        return favoriteDao.isFavorite(movieId, userId)
    }

    override suspend fun toggleFavorite(movie: MovieDetail) {
        val userId = currentUserId ?: "local"
        val isFav = favoriteDao.isFavorite(movie.id, userId).first()

        if (isFav) {
            favoriteDao.removeFavorite(movie.id, userId)
            if (userId != "local") {
                postgrest.from("favorites")
                    .delete {
                        filter {
                            eq("id", movie.id)
                            eq("user_id", userId)
                        }
                    }
            }
        } else {
            val entity = movie.toFavoriteEntity(userId)
            favoriteDao.addFavorite(entity)
            if (userId != "local") {
                postgrest.from("favorites").insert(entity)
            }
        }
    }

    override suspend fun syncFromCloud(): Flow<Resource<Unit>> = flow {
        val userId = currentUserId ?: return@flow
        emit(Resource.Loading)
        try {
            val cloudFavorites = postgrest.from("favorites")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeList<FavoriteMovieEntity>()

            cloudFavorites.forEach { favoriteDao.addFavorite(it) }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Đồng bộ đám mây thất bại"))
        }
    }

    override suspend fun clearAll() {
        val userId = currentUserId ?: "local"
        favoriteDao.clearAll(userId)
    }
}
