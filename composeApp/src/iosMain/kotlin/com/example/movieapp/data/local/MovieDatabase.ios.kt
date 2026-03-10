package com.example.movieapp.data.local

import androidx.room.*
import com.example.movieapp.AppUtil
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase> {
    val dbFilePath = NSHomeDirectory() + "/" + AppUtil.DATABASE_NAME
    return Room.databaseBuilder<MovieDatabase>(
        name = dbFilePath,
        factory = { MovieDatabase::class.instantiateImpl() }
    )
}
