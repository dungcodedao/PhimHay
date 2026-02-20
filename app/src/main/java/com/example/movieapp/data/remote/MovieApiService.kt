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
