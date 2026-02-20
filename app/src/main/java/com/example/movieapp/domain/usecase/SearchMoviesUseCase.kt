package com.example.movieapp.domain.usecase

import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase xử lý việc tìm kiếm phim.
 */
class SearchMoviesUseCase @Inject constructor(
    private val repository: IMovieRepository
) {
    operator fun invoke(query: String, page: Int = 1): Flow<Resource<List<Movie>>> {
        return repository.searchMovies(query, page)
    }

    fun getPaged(query: String): Flow<PagingData<Movie>> {
        return repository.getSearchMoviesPaging(query)
    }
}
