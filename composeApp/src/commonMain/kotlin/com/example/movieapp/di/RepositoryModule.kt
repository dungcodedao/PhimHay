package com.example.movieapp.di

import com.example.movieapp.data.repository.*
import com.example.movieapp.domain.repository.*
import org.koin.dsl.module

/**
 * Cung cấp các Repositories bằng Koin.
 */
val repositoryModule = module {
    single<IMovieRepository> { MovieRepositoryImpl(get(), get(), get(), get()) }
    single<IAuthRepository> { AuthRepositorySupabaseImpl(get(), get()) }
    single<IFavoriteRepository> { FavoriteRepositorySupabaseImpl(get(), get(), get()) }
    single<IRatingRepository> { RatingRepositoryImpl(get()) }
}
