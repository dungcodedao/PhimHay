package com.example.movieapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.movieapp.data.util.NetworkConnectivityObserver
import com.example.movieapp.presentation.components.NetworkStatusBanner
import platform.UIKit.UIViewController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

/**
 * Hàm khởi tạo UIViewController cho iOS.
 * Đây là điểm vào của ứng dụng Compose trên iPhone.
 */
fun MainViewController(): UIViewController = ComposeUIViewController {
    val iosApp = object : KoinComponent {
        val networkObserver: NetworkConnectivityObserver by inject()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        NetworkStatusBanner(iosApp.networkObserver)
        Box(modifier = Modifier.weight(1f)) {
            App()
        }
    }
}
