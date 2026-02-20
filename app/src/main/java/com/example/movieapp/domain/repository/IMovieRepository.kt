package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.*
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData

/**
 * Interface định nghĩa các hành động truy xuất dữ liệu phim.
 * Được sử dụng bởi UseCases trong Domain Layer.
 * Mọi phương thức trả về kết quả dưới dạng Flow để hỗ trợ lập trình Reactive.
 */
interface IMovieRepository {
    // Luồng dữ liệu cho Home
    fun getTrendingMovies(): Flow<Resource<List<Movie>>>
    fun getPopularMovies(page: Int = 1): Flow<Resource<List<Movie>>>
    fun getMovieGenres(): Flow<Resource<List<Genre>>>
    
    // Chi tiết phim
    fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>>
    fun getMovieVideos(movieId: Int): Flow<Resource<List<MovieVideo>>>
    fun getMovieCredits(movieId: Int): Flow<Resource<List<Cast>>>
    fun getSimilarMovies(movieId: Int): Flow<Resource<List<Movie>>>
    
    // Tìm kiếm & Lọc
    fun searchMovies(query: String, page: Int = 1): Flow<Resource<List<Movie>>>
    fun getMoviesByGenre(genreId: Int, page: Int = 1): Flow<Resource<List<Movie>>>
    
    // Paging 3
    fun getPopularMoviesPaging(): Flow<PagingData<Movie>>
    fun getMoviesByGenrePaging(genreId: Int): Flow<PagingData<Movie>>
    fun getSearchMoviesPaging(query: String): Flow<PagingData<Movie>>
    
    // Diễn viên
    fun getActorDetail(actorId: Int): Flow<Resource<ActorDetail>>

    // Lịch sử xem (Local Only)
    fun getWatchHistory(): Flow<List<Movie>>
    suspend fun addToHistory(movie: MovieDetail)
    suspend fun removeFromHistory(movieId: Int)
    suspend fun clearHistory()
}
