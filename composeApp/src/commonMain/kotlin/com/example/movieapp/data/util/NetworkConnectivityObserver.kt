package com.example.movieapp.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Quan sát tình trạng kết nối mạng.
 * Sử dụng expect/actual để triển khai riêng cho từng nền tảng.
 */
enum class ConnectivityStatus {
    Available, Unavailable, Losing, Lost
}

expect class NetworkConnectivityObserver() {
    fun observe(): Flow<ConnectivityStatus>
}
