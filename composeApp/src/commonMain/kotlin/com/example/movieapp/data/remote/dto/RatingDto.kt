package com.example.movieapp.data.remote.dto

import com.example.movieapp.domain.model.Rating
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("user_name") val userName: String,
    @SerialName("user_avatar") val userAvatar: String? = null,
    @SerialName("movie_id") val movieId: Int,
    @SerialName("rating") val rating: Int,
    @SerialName("comment") val comment: String,
    @SerialName("created_at") val createdAt: String? = null
)

fun RatingDto.toDomain() = Rating(
    id = id,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    movieId = movieId,
    rating = rating,
    comment = comment,
    createdAt = createdAt
)

fun Rating.toDto() = RatingDto(
    id = id,
    userId = userId,
    userName = userName,
    userAvatar = userAvatar,
    movieId = movieId,
    rating = rating,
    comment = comment,
    createdAt = createdAt
)
