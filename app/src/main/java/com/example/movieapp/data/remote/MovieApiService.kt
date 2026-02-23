package com.example.movieapp.data.remote

import com.example.movieapp.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TMDb API Service - Định nghĩa các endpoints để truy xuất dữ liệu từ Remote Server.
 */
interface MovieApiService {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    // TV Series Endpoints
    @GET("trending/tv/week")
    suspend fun getTrendingTV(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("tv/popular")
    suspend fun getPopularTV(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTV(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): MovieDetailDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): VideoListResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): CreditsResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    // ========== TV SERIES DETAIL ENDPOINTS ==========
    @GET("tv/{tv_id}")
    suspend fun getTVDetail(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): MovieDetailDto

    @GET("tv/{tv_id}/videos")
    suspend fun getTVVideos(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): VideoListResponse

    @GET("tv/{tv_id}/credits")
    suspend fun getTVCredits(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): CreditsResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTV(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): GenreListResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("language") language: String = "vi-VN",
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("person/{person_id}")
    suspend fun getActorDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): ActorDetailDto

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "vi-VN"
    ): ActorMoviesResponse
}
