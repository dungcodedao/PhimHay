package com.example.movieapp.data.remote

import com.example.movieapp.AppUtil
import com.example.movieapp.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Triển khai dịch vụ API bằng Ktor Client thay cho Retrofit để hỗ trợ Kotlin Multiplatform.
 */
class KtorMovieService(private val client: HttpClient) {
    private val baseUrl = "https://api.themoviedb.org/3/"

    private suspend inline fun <reified T> safeGet(
        endpoint: String,
        params: Map<String, Any?> = emptyMap()
    ): T {
        return client.get(baseUrl + endpoint) {
            parameter("api_key", AppUtil.TMDB_API_KEY)
            parameter("language", "vi-VN")
            params.forEach { (key, value) ->
                if (value != null) parameter(key, value)
            }
        }.body()
    }

    suspend fun getTrendingMovies(page: Int = 1): MovieListResponse = 
        safeGet("trending/movie/week", mapOf("page" to page))

    suspend fun getPopularMovies(page: Int = 1): MovieListResponse = 
        safeGet("movie/popular", mapOf("page" to page))

    suspend fun getTopRatedMovies(page: Int = 1): MovieListResponse = 
        safeGet("movie/top_rated", mapOf("page" to page))

    suspend fun getUpcomingMovies(page: Int = 1): MovieListResponse = 
        safeGet("movie/upcoming", mapOf("page" to page))

    suspend fun getNowPlayingMovies(page: Int = 1): MovieListResponse = 
        safeGet("movie/now_playing", mapOf("page" to page))

    // TV Series
    suspend fun getTrendingTV(page: Int = 1): MovieListResponse = 
        safeGet("trending/tv/week", mapOf("page" to page))

    suspend fun getPopularTV(page: Int = 1): MovieListResponse = 
        safeGet("tv/popular", mapOf("page" to page))

    suspend fun getTopRatedTV(page: Int = 1): MovieListResponse = 
        safeGet("tv/top_rated", mapOf("page" to page))

    suspend fun getMovieDetail(movieId: Int): MovieDetailDto = 
        safeGet("movie/$movieId")

    suspend fun getMovieVideos(movieId: Int, language: String = "vi-VN"): VideoListResponse = 
        safeGet("movie/$movieId/videos", mapOf("language" to language))

    suspend fun getMovieCredits(movieId: Int): CreditsResponse = 
        safeGet("movie/$movieId/credits")

    suspend fun getSimilarMovies(movieId: Int, page: Int = 1): MovieListResponse = 
        safeGet("movie/$movieId/similar", mapOf("page" to page))

    // TV Detail
    suspend fun getTVDetail(tvId: Int): MovieDetailDto = 
        safeGet("tv/$tvId")

    suspend fun getTVVideos(tvId: Int, language: String = "vi-VN"): VideoListResponse = 
        safeGet("tv/$tvId/videos", mapOf("language" to language))

    suspend fun getTVCredits(tvId: Int): CreditsResponse = 
        safeGet("tv/$tvId/credits")

    suspend fun getSimilarTV(tvId: Int, page: Int = 1): MovieListResponse = 
        safeGet("tv/$tvId/similar", mapOf("page" to page))

    suspend fun searchMovies(query: String, page: Int = 1): MovieListResponse = 
        safeGet("search/movie", mapOf("query" to query, "page" to page))

    suspend fun getMovieGenres(): GenreListResponse = 
        safeGet("genre/movie/list")

    suspend fun getMoviesByGenre(genreId: Int, page: Int = 1): MovieListResponse = 
        safeGet("discover/movie", mapOf("with_genres" to genreId, "page" to page))

    suspend fun getActorDetail(personId: Int): ActorDetailDto = 
        safeGet("person/$personId")

    suspend fun getActorMovies(personId: Int): ActorMoviesResponse = 
        safeGet("person/$personId/movie_credits")
}
