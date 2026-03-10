package com.example.movieapp.data.mapper

import com.example.movieapp.AppUtil
import com.example.movieapp.data.local.entity.*
import com.example.movieapp.data.remote.dto.*
import com.example.movieapp.domain.model.*

/**
 * Mappers: Chuyển đổi dữ liệu giữa các Layer (DTO ↔ Domain ↔ Entity).
 * Đảm bảo tính đóng gói và độc lập của Domain Layer.
 */

// DTO → Domain (tự động phát hiện TV series dựa trên name/firstAirDate nếu không được chỉ định)
fun MovieDto.toDomain(isTV: Boolean? = null): Movie {
    val detectedIsTV = isTV ?: (title == null && name != null)
    return Movie(
        id = id,
        title = title ?: name ?: "",
        overview = overview,
        posterUrl = AppUtil.posterUrl(posterPath),
        backdropUrl = AppUtil.backdropUrl(backdropPath),
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate ?: firstAirDate ?: "",
        genreIds = genreIds,
        isTV = detectedIsTV
    )
}

// DTO → Entity (để lưu cache, tự động nhận biết TV qua category name)
fun MovieDto.toEntity(category: String): MovieEntity = MovieEntity(
    id = id,
    title = title ?: name ?: "",
    overview = overview,
    posterPath = posterPath ?: "",
    backdropPath = backdropPath ?: "",
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate ?: firstAirDate ?: "",
    category = category,
    isTV = category.contains("tv", ignoreCase = true)
)

// Entity → Domain (đọc từ cache, phục hồi isTV flag)
fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = AppUtil.posterUrl(posterPath),
    backdropUrl = AppUtil.backdropUrl(backdropPath),
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate,
    genreIds = emptyList(),
    isTV = isTV
)

fun FavoriteMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = AppUtil.posterUrl(posterPath),
    backdropUrl = AppUtil.backdropUrl(backdropPath),
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate,
    genreIds = emptyList(),
    isTV = isTV
)

// Detail DTO → Domain
fun MovieDetailDto.toDomain(isTV: Boolean = false): MovieDetail = MovieDetail(
    id = id,
    title = title ?: name ?: "",
    overview = overview,
    posterUrl = AppUtil.posterUrl(posterPath),
    backdropUrl = AppUtil.backdropUrl(backdropPath),
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate ?: firstAirDate ?: "",
    runtime = runtime,
    genres = genres.map { it.toDomain() },
    status = status,
    tagline = tagline,
    isTV = isTV
)

fun GenreDto.toDomain(): Genre = Genre(id = id, name = name)

fun VideoDto.toDomain(): MovieVideo = MovieVideo(
    id = id, key = key, name = name, site = site, type = type
)

fun CastDto.toDomain(): Cast = Cast(
    id = id,
    name = name,
    character = character,
    profileUrl = AppUtil.profileUrl(profilePath)
)

// Domain → Favorite Entity
fun MovieDetail.toFavoriteEntity(userId: String): FavoriteMovieEntity = FavoriteMovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = "/" + posterUrl.substringAfterLast("/"),
    backdropPath = "/" + backdropUrl.substringAfterLast("/"),
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate,
    userId = userId,
    isTV = isTV
)

// Actor Detail DTO → Domain
fun ActorDetailDto.toDomain(movies: List<MovieDto>): ActorDetail = ActorDetail(
    id = id,
    name = name,
    biography = biography,
    birthday = birthday,
    placeOfBirth = placeOfBirth,
    profileUrl = AppUtil.profileUrl(profilePath),
    knownFor = knownFor,
    movies = movies.map { it.toDomain() }
)

fun MovieDetail.toHistoryEntity(): HistoryMovieEntity = HistoryMovieEntity(
    id = id,
    title = title,
    posterPath = "/" + posterUrl.substringAfterLast("/"),
    voteAverage = voteAverage,
    isTV = isTV,
    watchedAt = System.currentTimeMillis()
)
