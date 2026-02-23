package com.example.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.AppUtil
import com.example.movieapp.data.local.PreferenceManager
import com.example.movieapp.data.local.dao.HistoryDao
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.mapper.*
import com.example.movieapp.data.paging.MoviePagingSource
import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.data.remote.dto.MovieListResponse
import com.example.movieapp.domain.model.*
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Triển khai cụ thể của IMovieRepository.
 * Đảm bảo Single Source of Truth (SSOT) bằng cách kết hợp Room và Retrofit.
 * Mọi kết quả được trả về dưới dạng Flow<Resource<T>>.
 */
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApiService,
    private val movieDao: MovieDao,
    private val historyDao: HistoryDao,
    private val preferenceManager: PreferenceManager
) : IMovieRepository {

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("trending") {
        api.getTrendingMovies(apiKey = AppUtil.TMDB_API_KEY)
    }

    override fun getPopularMovies(page: Int): Flow<Resource<List<Movie>>> = fetchAndCache("popular") {
        api.getPopularMovies(apiKey = AppUtil.TMDB_API_KEY, page = page)
    }

    override fun getTopRatedMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("top_rated") {
        api.getTopRatedMovies(apiKey = AppUtil.TMDB_API_KEY)
    }

    override fun getUpcomingMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("upcoming") {
        api.getUpcomingMovies(apiKey = AppUtil.TMDB_API_KEY)
    }

    override fun getNowPlayingMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("now_playing") {
        api.getNowPlayingMovies(apiKey = AppUtil.TMDB_API_KEY)
    }

    override fun getTrendingTV(): Flow<Resource<List<Movie>>> = fetchAndCache("tv_trending") {
        api.getTrendingTV(apiKey = AppUtil.TMDB_API_KEY)
    }

    override fun getPopularTV(): Flow<Resource<List<Movie>>> = fetchAndCache("tv_popular") {
        api.getPopularTV(apiKey = AppUtil.TMDB_API_KEY)
    }

    override fun getTopRatedTV(): Flow<Resource<List<Movie>>> = fetchAndCache("tv_top_rated") {
        api.getTopRatedTV(apiKey = AppUtil.TMDB_API_KEY)
    }

    /**
     * Helper function để thực hiện fetch API và lưu Cache vào DB.
     * Dùng cho các danh sách phim trang chủ.
     */
    private fun fetchAndCache(
        category: String,
        fetcher: suspend () -> MovieListResponse
    ): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)

        val localData = movieDao.getMoviesByCategoryOnce(category)
        if (localData.isNotEmpty()) {
            emit(Resource.Success(localData.map { it.toDomain() }))
        }

        try {
            val response = fetcher()
            val entities = response.results.map { it.toEntity(category) }
            movieDao.clearMoviesByCategory(category)
            movieDao.insertMovies(entities)
            
            val newData = movieDao.getMoviesByCategoryOnce(category)
            emit(Resource.Success(newData.map { it.toDomain() }))
        } catch (e: Exception) {
            if (localData.isEmpty()) {
                emit(Resource.Error(e.localizedMessage ?: "Lỗi tải dữ liệu"))
            }
        }
    }

    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMovieDetail(movieId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải chi tiết phim"))
        }
    }

    override fun getMovieVideos(movieId: Int): Flow<Resource<List<MovieVideo>>> = flow {
        emit(Resource.Loading)
        try {
            var response = api.getMovieVideos(movieId, AppUtil.TMDB_API_KEY, "vi-VN")
            if (response.results.isEmpty()) {
                response = api.getMovieVideos(movieId, AppUtil.TMDB_API_KEY, "en-US")
            }
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải video"))
        }
    }

    override fun getMovieCredits(movieId: Int): Flow<Resource<List<Cast>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMovieCredits(movieId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.cast.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải diễn viên"))
        }
    }

    override fun getSimilarMovies(movieId: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getSimilarMovies(movieId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải phim tương tự"))
        }
    }

    // ==================== TV SERIES DETAIL ====================
    override fun getTVDetail(tvId: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getTVDetail(tvId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải chi tiết phim bộ"))
        }
    }

    override fun getTVVideos(tvId: Int): Flow<Resource<List<MovieVideo>>> = flow {
        emit(Resource.Loading)
        try {
            var response = api.getTVVideos(tvId, AppUtil.TMDB_API_KEY, "vi-VN")
            if (response.results.isEmpty()) {
                response = api.getTVVideos(tvId, AppUtil.TMDB_API_KEY, "en-US")
            }
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải video phim bộ"))
        }
    }

    override fun getTVCredits(tvId: Int): Flow<Resource<List<Cast>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getTVCredits(tvId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.cast.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải diễn viên phim bộ"))
        }
    }

    override fun getSimilarTV(tvId: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getSimilarTV(tvId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.results.map { it.toDomain(isTV = true) }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải phim bộ tương tự"))
        }
    }

    override fun searchMovies(query: String, page: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.searchMovies(AppUtil.TMDB_API_KEY, query, page = page)
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tìm kiếm"))
        }
    }

    override fun getMovieGenres(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMovieGenres(AppUtil.TMDB_API_KEY)
            emit(Resource.Success(response.genres.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải thể loại"))
        }
    }

    override fun getMoviesByGenre(genreId: Int, page: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMoviesByGenre(AppUtil.TMDB_API_KEY, genreId, page = page)
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải phim theo thể loại"))
        }
    }

    // Paging 3
    override fun getPopularMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { MoviePagingSource(api, "popular") }
        ).flow
    }

    override fun getMoviesByGenrePaging(genreId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { MoviePagingSource(api, "genre", genreId) }
        ).flow
    }

    override fun getSearchMoviesPaging(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { MoviePagingSource(api, query = query) }
        ).flow
    }

    override fun getActorDetail(actorId: Int): Flow<Resource<ActorDetail>> = flow {
        emit(Resource.Loading)
        try {
            val detail = api.getActorDetail(actorId, AppUtil.TMDB_API_KEY)
            val movies = api.getActorMovies(actorId, AppUtil.TMDB_API_KEY)
            emit(Resource.Success(detail.toDomain(movies.cast)))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Lỗi tải thông tin diễn viên"))
        }
    }

    // Watch History
    override fun getWatchHistory(): Flow<List<Movie>> = flow {
        historyDao.getWatchHistory().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    // Mapper helper cho History
    private fun com.example.movieapp.data.local.entity.HistoryMovieEntity.toDomain(): Movie = Movie(
        id = id,
        title = title,
        overview = "",
        posterUrl = AppUtil.posterUrl(posterPath),
        backdropUrl = "",
        voteAverage = voteAverage,
        voteCount = 0,
        releaseDate = "",
        genreIds = emptyList()
    )

    override suspend fun addToHistory(movie: MovieDetail) {
        historyDao.addToHistory(movie.toHistoryEntity())
    }

    override suspend fun removeFromHistory(movieId: Int) {
        historyDao.removeFromHistory(movieId)
    }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}
