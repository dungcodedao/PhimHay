package com.example.movieapp.data.repository

import com.example.movieapp.data.remote.dto.RatingDto
import com.example.movieapp.data.remote.dto.toDomain
import com.example.movieapp.data.remote.dto.toDto
import com.example.movieapp.domain.model.Rating
import com.example.movieapp.domain.repository.IRatingRepository
import com.example.movieapp.domain.util.Resource
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow




class RatingRepositoryImpl (
    private val postgrest: Postgrest
) : IRatingRepository {

    override fun getMovieRatings(movieId: Int): Flow<Resource<List<Rating>>> = flow {
        emit(Resource.Loading)
        try {
            val response = postgrest.from("ratings").select {
                filter {
                    eq("movie_id", movieId)
                }
            }.decodeList<RatingDto>()
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Không thể tải đánh giá"))
        }
    }

    override suspend fun submitRating(rating: Rating): Resource<Unit> {
        return try {
            postgrest.from("ratings").upsert(rating.toDto())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Không thể gửi đánh giá")
        }
    }

    override suspend fun getUserRatingForMovie(movieId: Int, userId: String): Resource<Rating?> {
        return try {
            val response = postgrest.from("ratings").select {
                filter {
                    eq("movie_id", movieId)
                    eq("user_id", userId)
                }
            }.decodeSingleOrNull<RatingDto>()
            Resource.Success(response?.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Lỗi kiểm tra đánh giá cá nhân")
        }
    }
}
