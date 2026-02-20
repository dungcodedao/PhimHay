package com.example.movieapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Objects (DTO) - Đại diện cho cấu trúc dữ liệu thô từ TMDb API.
 */
@Serializable
data class MovieListResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class MovieDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("popularity") val popularity: Double = 0.0,
    @SerialName("adult") val adult: Boolean = false
)

@Serializable
data class MovieDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("runtime") val runtime: Int = 0,
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("status") val status: String = "",
    @SerialName("tagline") val tagline: String = ""
)

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

@Serializable
data class GenreListResponse(
    @SerialName("genres") val genres: List<GenreDto>
)

@Serializable
data class VideoListResponse(
    @SerialName("id") val id: Int,
    @SerialName("results") val results: List<VideoDto>
)

@Serializable
data class VideoDto(
    @SerialName("id") val id: String,
    @SerialName("key") val key: String,
    @SerialName("name") val name: String,
    @SerialName("site") val site: String,
    @SerialName("type") val type: String
)

@Serializable
data class CreditsResponse(
    @SerialName("id") val id: Int,
    @SerialName("cast") val cast: List<CastDto>
)

@Serializable
data class CastDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("character") val character: String,
    @SerialName("profile_path") val profilePath: String? = null
)

@Serializable
data class ActorDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("biography") val biography: String = "",
    @SerialName("birthday") val birthday: String? = null,
    @SerialName("place_of_birth") val placeOfBirth: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("known_for_department") val knownFor: String = ""
)

@Serializable
data class ActorMoviesResponse(
    @SerialName("cast") val cast: List<MovieDto>
)
