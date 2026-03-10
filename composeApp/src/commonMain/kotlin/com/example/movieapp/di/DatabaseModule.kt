package com.example.movieapp.di

import com.example.movieapp.data.local.getDatabaseBuilder
import com.example.movieapp.data.local.getRoomDatabase
import org.koin.dsl.module

/**
 * Cung cấp Room Database và các DAOs bằng Koin.
 */
val databaseModule = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
    
    single { get<com.example.movieapp.data.local.MovieDatabase>().movieDao() }
    single { get<com.example.movieapp.data.local.MovieDatabase>().favoriteDao() }
    single { get<com.example.movieapp.data.local.MovieDatabase>().historyDao() }
    
    single { com.example.movieapp.data.local.PreferenceManager() }
}
