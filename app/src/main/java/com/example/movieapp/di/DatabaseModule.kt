package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.AppUtil
import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.local.dao.FavoriteDao
import com.example.movieapp.data.local.dao.HistoryDao
import com.example.movieapp.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Cung cấp Room Database và các DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            AppUtil.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMovieDao(db: MovieDatabase): MovieDao = db.movieDao()

    @Provides
    fun provideFavoriteDao(db: MovieDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideHistoryDao(db: MovieDatabase): HistoryDao = db.historyDao()
}
