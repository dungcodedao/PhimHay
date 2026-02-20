package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Room Entities - Đại diện cho cấu trúc bảng trong cơ sở dữ liệu local (SQLite).
 */
@Entity(tableName = "cached_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val category: String, // "trending" hoặc "popular"
    val cachedAt: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey 
    val id: Int,
    val title: String,
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("user_id")
    val userId: String,
    @SerialName("added_at")
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "watch_history")
data class HistoryMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val watchedAt: Long = System.currentTimeMillis()
)
