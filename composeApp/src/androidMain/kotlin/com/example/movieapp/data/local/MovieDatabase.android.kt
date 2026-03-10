package com.example.movieapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.AppUtil
import com.example.movieapp.MovieApp

actual fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase> {
    val appContext = MovieApp.instance.applicationContext
    val dbFile = appContext.getDatabasePath(AppUtil.DATABASE_NAME)
    return Room.databaseBuilder<MovieDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
