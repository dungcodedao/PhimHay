package com.example.movieapp.domain.model

/**
 * Domain Models - dùng thuần túy trong UI và Business Logic theo chuẩn Clean Architecture.
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val genreIds: List<Int>
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<Genre>,
    val status: String,
    val tagline: String
)

data class Genre(
    val id: Int,
    val name: String
)

data class MovieVideo(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
) {
    val isYoutubeTrailer: Boolean
        get() = site.equals("YouTube", ignoreCase = true) &&
                type.equals("Trailer", ignoreCase = true)
}

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profileUrl: String?
)

data class ActorDetail(
    val id: Int,
    val name: String,
    val biography: String,
    val birthday: String?,
    val placeOfBirth: String?,
    val profileUrl: String?,
    val knownFor: String,
    val movies: List<Movie> = emptyList()
)
