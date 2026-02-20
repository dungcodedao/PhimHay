package com.example.movieapp

/**
 * App Constants & Resource wrapper.
 */
object AppUtil {

    // TMDb API
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val TMDB_API_KEY = "3dbb962239ad9b7467ed42d9422af0f2"
    const val BASE_URL = "https://api.themoviedb.org/3/"

    // Supabase Config
    const val SUPABASE_URL = "https://xooptlnxuqdnkcrioxjh.supabase.co"
    const val SUPABASE_ANON_KEY = "sb_publishable_lYuTGittmqRsiE1StiHdbQ_qewcdSig"

    // Room
    const val DATABASE_NAME = "movie_app_database"

    // Image Sizes
    const val POSTER_SIZE_W500 = "w500"
    const val BACKDROP_SIZE_ORIGINAL = "original"
    const val PROFILE_SIZE_W185 = "w185"

    fun posterUrl(path: String?): String =
        path?.let { "${TMDB_IMAGE_BASE_URL}${POSTER_SIZE_W500}$it" } ?: ""

    fun backdropUrl(path: String?): String =
        path?.let { "${TMDB_IMAGE_BASE_URL}${BACKDROP_SIZE_ORIGINAL}$it" } ?: ""

    fun profileUrl(path: String?): String =
        path?.let { "${TMDB_IMAGE_BASE_URL}${PROFILE_SIZE_W185}$it" } ?: ""
}
