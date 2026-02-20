package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.*
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase tập hợp mọi thông tin cần thiết cho màn hình chi tiết phim.
 * Toàn bộ dữ liệu được trả về dưới dạng Flow.
 */
class GetMovieDetailUseCase @Inject constructor(
    private val repository: IMovieRepository
) {
    fun getDetail(movieId: Int): Flow<Resource<MovieDetail>> = repository.getMovieDetail(movieId)
    
    fun getVideos(movieId: Int): Flow<Resource<List<MovieVideo>>> = repository.getMovieVideos(movieId)
    
    fun getCast(movieId: Int): Flow<Resource<List<Cast>>> = repository.getMovieCredits(movieId)
    
    fun getSimilar(movieId: Int): Flow<Resource<List<Movie>>> = repository.getSimilarMovies(movieId)
}
