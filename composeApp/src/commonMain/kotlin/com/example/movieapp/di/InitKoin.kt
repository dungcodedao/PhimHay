package com.example.movieapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Hàm khởi tạo Koin dùng chung cho cả Android và iOS.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        networkModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        viewModelModule
    )
}

// Gọi từ iOS
fun initKoin() = initKoin {}
