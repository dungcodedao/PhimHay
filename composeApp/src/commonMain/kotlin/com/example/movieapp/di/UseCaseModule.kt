package com.example.movieapp.di

import com.example.movieapp.domain.usecase.*
import org.koin.dsl.module

/**
 * Cung cấp các Use Cases bằng Koin.
 */
val useCaseModule = module {
    factory { GetHomeMoviesUseCase(get()) }
    factory { GetMovieDetailUseCase(get()) }
    factory { ManageFavoriteUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
}
