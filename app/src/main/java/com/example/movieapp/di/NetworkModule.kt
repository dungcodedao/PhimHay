package com.example.movieapp.di

import com.example.movieapp.AppUtil
import com.example.movieapp.data.remote.MovieApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Cung cấp các instance liên quan đến Network (Retrofit, Supabase).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
    }

    // TMDb API
    @Provides
    @Singleton
    fun provideMovieApiService(json: Json): MovieApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(AppUtil.TMDB_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(MovieApiService::class.java)
    }

    // Supabase
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = AppUtil.SUPABASE_URL,
            supabaseKey = AppUtil.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth = client.pluginManager.getPlugin(Auth)

    @Provides
    @Singleton
    fun provideSupabasePostgrest(client: SupabaseClient): Postgrest = client.pluginManager.getPlugin(Postgrest)

    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage = client.pluginManager.getPlugin(Storage)

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context
    ): com.example.movieapp.data.util.NetworkConnectivityObserver {
        return com.example.movieapp.data.util.NetworkConnectivityObserver(context)
    }
}
