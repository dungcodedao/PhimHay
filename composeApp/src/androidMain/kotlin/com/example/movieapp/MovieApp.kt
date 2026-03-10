package com.example.movieapp

import android.app.Application
import com.example.movieapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MovieApp : Application() {
    companion object {
        lateinit var instance: MovieApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        initKoin {
            androidLogger()
            androidContext(this@MovieApp)
        }
    }
}
