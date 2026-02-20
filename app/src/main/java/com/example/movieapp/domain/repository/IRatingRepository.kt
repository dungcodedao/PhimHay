package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Rating
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface IRatingRepository {
    fun getMovieRatings(movieId: Int): Flow<Resource<List<Rating>>>
    suspend fun submitRating(rating: Rating): Resource<Unit>
    suspend fun getUserRatingForMovie(movieId: Int, userId: String): Resource<Rating?>
}
