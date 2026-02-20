package com.example.movieapp.di

import com.example.movieapp.data.repository.*
import com.example.movieapp.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Liên kết các Interface trong Domain với bản thực thi trong Data layer.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): IMovieRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositorySupabaseImpl
    ): IFavoriteRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositorySupabaseImpl
    ): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindRatingRepository(
        ratingRepositoryImpl: RatingRepositoryImpl
    ): IRatingRepository
}
