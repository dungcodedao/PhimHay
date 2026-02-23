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
    fun getDetail(movieId: Int, isTV: Boolean = false): Flow<Resource<MovieDetail>> =
        if (isTV) repository.getTVDetail(movieId)
        else repository.getMovieDetail(movieId)

    fun getVideos(movieId: Int, isTV: Boolean = false): Flow<Resource<List<MovieVideo>>> =
        if (isTV) repository.getTVVideos(movieId)
        else repository.getMovieVideos(movieId)

    fun getCast(movieId: Int, isTV: Boolean = false): Flow<Resource<List<Cast>>> =
        if (isTV) repository.getTVCredits(movieId)
        else repository.getMovieCredits(movieId)

    fun getSimilar(movieId: Int, isTV: Boolean = false): Flow<Resource<List<Movie>>> =
        if (isTV) repository.getSimilarTV(movieId)
        else repository.getSimilarMovies(movieId)
}
