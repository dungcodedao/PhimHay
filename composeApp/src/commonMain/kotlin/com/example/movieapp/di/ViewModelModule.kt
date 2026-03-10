package com.example.movieapp.di

import com.example.movieapp.presentation.viewmodel.*
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Cung cấp các ViewModels bằng Koin.
 * Hỗ trợ tạo ViewModel dùng chung cho cả Android và iOS.
 */
val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { DetailViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { ActorViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
}
