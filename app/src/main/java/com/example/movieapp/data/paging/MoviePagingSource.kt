package com.example.movieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.AppUtil
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.domain.model.Movie

/**
 * PagingSource cho danh sách phim hỗ trợ tải vô hạn.
 */
class MoviePagingSource(
    private val api: MovieApiService,
    private val category: String? = null,
    private val genreId: Int? = null,
    private val query: String? = null
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = when {
                query != null -> api.searchMovies(AppUtil.TMDB_API_KEY, query, page = page)
                genreId != null -> api.getMoviesByGenre(AppUtil.TMDB_API_KEY, genreId, page = page)
                else -> api.getPopularMovies(AppUtil.TMDB_API_KEY, page = page)
            }
            
            val movies = response.results.map { it.toDomain() }
            
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty() || page >= response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
