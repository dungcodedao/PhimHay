package com.example.movieapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val id: String? = null,
    val userId: String,
    val userName: String,
    val userAvatar: String? = null,
    val movieId: Int,
    val rating: Int, // 1 to 5 stars
    val comment: String,
    val createdAt: String? = null
)
