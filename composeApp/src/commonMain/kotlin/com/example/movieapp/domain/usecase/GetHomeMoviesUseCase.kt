package com.example.movieapp.domain.usecase

import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * UseCase lấy dữ liệu cho màn hình Home (Trending & Popular).
 */
class GetHomeMoviesUseCase(
    private val repository: IMovieRepository
) {
    fun getTrending(): Flow<Resource<List<Movie>>> = repository.getTrendingMovies()
    fun getPopular(): Flow<Resource<List<Movie>>> = repository.getPopularMovies()
    fun getTopRated(): Flow<Resource<List<Movie>>> = repository.getTopRatedMovies()
    fun getUpcoming(): Flow<Resource<List<Movie>>> = repository.getUpcomingMovies()
    
    // TV Series
    fun getTrendingTV(): Flow<Resource<List<Movie>>> = repository.getTrendingTV()
    fun getPopularTV(): Flow<Resource<List<Movie>>> = repository.getPopularTV()

    fun getByGenre(genreId: Int): Flow<Resource<List<Movie>>> = repository.getMoviesByGenre(genreId)
    
    // Paging
    fun getPopularPaging(): Flow<PagingData<Movie>> = repository.getPopularMoviesPaging()
    
    fun getByGenrePaging(genreId: Int): Flow<PagingData<Movie>> = repository.getMoviesByGenrePaging(genreId)
}
