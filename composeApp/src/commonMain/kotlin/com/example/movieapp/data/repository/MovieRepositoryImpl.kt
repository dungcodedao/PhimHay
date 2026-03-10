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
import com.example.movieapp.data.remote.KtorMovieService
import com.example.movieapp.data.remote.dto.MovieListResponse
import com.example.movieapp.domain.model.*
import com.example.movieapp.domain.repository.IMovieRepository
import com.example.movieapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Triển khai cụ thể của IMovieRepository.
 * Đảm bảo Single Source of Truth (SSOT) bằng cách kết hợp Room và Ktor.
 * Mọi kết quả được trả về dưới dạng Flow<Resource<T>>.
 */
class MovieRepositoryImpl(
    private val api: KtorMovieService,
    private val movieDao: MovieDao,
    private val historyDao: HistoryDao,
    private val preferenceManager: PreferenceManager
) : IMovieRepository {

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("trending") {
        api.getTrendingMovies()
    }

    override fun getPopularMovies(page: Int): Flow<Resource<List<Movie>>> = fetchAndCache("popular") {
        api.getPopularMovies(page = page)
    }

    override fun getTopRatedMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("top_rated") {
        api.getTopRatedMovies()
    }

    override fun getUpcomingMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("upcoming") {
        api.getUpcomingMovies()
    }

    override fun getNowPlayingMovies(): Flow<Resource<List<Movie>>> = fetchAndCache("now_playing") {
        api.getNowPlayingMovies()
    }

    override fun getTrendingTV(): Flow<Resource<List<Movie>>> = fetchAndCache("tv_trending") {
        api.getTrendingTV()
    }

    override fun getPopularTV(): Flow<Resource<List<Movie>>> = fetchAndCache("tv_popular") {
        api.getPopularTV()
    }

    override fun getTopRatedTV(): Flow<Resource<List<Movie>>> = fetchAndCache("tv_top_rated") {
        api.getTopRatedTV()
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
            movieDao.clearMoviesByCategory(category)
            movieDao.insertMovies(response.results.map { it.toEntity(category) })
            
            val newData = movieDao.getMoviesByCategoryOnce(category)
            emit(Resource.Success(newData.map { it.toDomain() }))
        } catch (e: Exception) {
            if (localData.isEmpty()) {
                emit(Resource.Error(e.message ?: "Lỗi tải dữ liệu"))
            }
        }
    }

    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)
        
        // 1. Phục hồi từ Cache trươc
        val cachedDetail = movieDao.getMovieDetailById(movieId)
        if (cachedDetail != null) {
            emit(Resource.Success(cachedDetail.toDomain()))
        }

        try {
            // 2. Tải mơi tư API
            val response = api.getMovieDetail(movieId)
            val domainDetail = response.toDomain()
            
            // 3. Lưu lại vào Cache
            movieDao.insertMovieDetail(domainDetail.toEntity())
            
            emit(Resource.Success(domainDetail))
        } catch (e: Exception) {
            // Nêu trail lôi và không có cache thì mơi báo lôi
            if (cachedDetail == null) {
                emit(Resource.Error(e.message ?: "Lỗi tải chi tiết phim"))
            }
        }
    }

    // --- Helper Mappers cho Chi Tiêt ---
    private fun com.example.movieapp.data.local.entity.MovieDetailEntity.toDomain(): MovieDetail = MovieDetail(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterPath ?: "",
        backdropUrl = backdropPath ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        runtime = runtime,
        genres = genres.split(",").filter { it.isNotBlank() }.map { Genre(0, it) },
        status = status,
        tagline = tagline,
        isTV = isTV
    )

    private fun MovieDetail.toEntity(): com.example.movieapp.data.local.entity.MovieDetailEntity = com.example.movieapp.data.local.entity.MovieDetailEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterUrl,
        backdropPath = backdropUrl,
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        runtime = runtime,
        genres = genres.joinToString(",") { it.name },
        status = status,
        tagline = tagline,
        isTV = isTV
    )

    override fun getMovieVideos(movieId: Int): Flow<Resource<List<MovieVideo>>> = flow {
        emit(Resource.Loading)
        try {
            var response = api.getMovieVideos(movieId, "vi-VN")
            if (response.results.isEmpty()) {
                response = api.getMovieVideos(movieId, "en-US")
            }
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải video"))
        }
    }

    override fun getMovieCredits(movieId: Int): Flow<Resource<List<Cast>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMovieCredits(movieId)
            emit(Resource.Success(response.cast.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải diễn viên"))
        }
    }

    override fun getSimilarMovies(movieId: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getSimilarMovies(movieId)
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải phim tương tự"))
        }
    }

    // ==================== TV SERIES DETAIL ====================
    override fun getTVDetail(tvId: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)

        // 1. Phục hồi từ Cache trươc (dùng chung table Detail)
        val cachedDetail = movieDao.getMovieDetailById(tvId)
        if (cachedDetail != null) {
            emit(Resource.Success(cachedDetail.toDomain()))
        }

        try {
            val response = api.getTVDetail(tvId)
            val domainDetail = response.toDomain(isTV = true)
            
            // 2. Lưu lại vào Cache
            movieDao.insertMovieDetail(domainDetail.toEntity())
            
            emit(Resource.Success(domainDetail))
        } catch (e: Exception) {
            if (cachedDetail == null) {
                emit(Resource.Error(e.message ?: "Lỗi tải chi tiết phim bộ"))
            }
        }
    }

    override fun getTVVideos(tvId: Int): Flow<Resource<List<MovieVideo>>> = flow {
        emit(Resource.Loading)
        try {
            var response = api.getTVVideos(tvId, "vi-VN")
            if (response.results.isEmpty()) {
                response = api.getTVVideos(tvId, "en-US")
            }
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải video phim bộ"))
        }
    }

    override fun getTVCredits(tvId: Int): Flow<Resource<List<Cast>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getTVCredits(tvId)
            emit(Resource.Success(response.cast.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải diễn viên phim bộ"))
        }
    }

    override fun getSimilarTV(tvId: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getSimilarTV(tvId)
            emit(Resource.Success(response.results.map { it.toDomain(isTV = true) }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải phim bộ tương tự"))
        }
    }

    override fun searchMovies(query: String, page: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.searchMovies(query, page = page)
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tìm kiếm"))
        }
    }

    override fun getMovieGenres(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMovieGenres()
            emit(Resource.Success(response.genres.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải thể loại"))
        }
    }

    override fun getMoviesByGenre(genreId: Int, page: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getMoviesByGenre(genreId, page = page)
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải phim theo thể loại"))
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
            val detail = api.getActorDetail(actorId)
            val movies = api.getActorMovies(actorId)
            emit(Resource.Success(detail.toDomain(movies.cast)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Lỗi tải thông tin diễn viên"))
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
