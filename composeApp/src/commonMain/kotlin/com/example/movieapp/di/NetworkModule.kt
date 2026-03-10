package com.example.movieapp.di

import com.example.movieapp.AppUtil
import com.example.movieapp.data.remote.KtorMovieService
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.movieapp.data.util.NetworkConnectivityObserver
import org.koin.dsl.module

/**
 * Cung cấp các instance liên quan đến Network bằng Koin.
 */
val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single { KtorMovieService(get()) }

    // Supabase client
    single {
        createSupabaseClient(
            supabaseUrl = AppUtil.SUPABASE_URL,
            supabaseKey = AppUtil.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }

    // Expose từng plugin Supabase riêng lẻ cho Koin DI
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().postgrest }
    single { get<SupabaseClient>().realtime }
    single { get<SupabaseClient>().storage }
    
    // Observer trạng thái mạng
    single<NetworkConnectivityObserver> { NetworkConnectivityObserver() }
}
