package com.example.movieapp.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.data.util.ConnectivityStatus
import com.example.movieapp.data.util.NetworkConnectivityObserver

import com.example.movieapp.R

@Composable
fun NetworkStatusBanner(networkObserver: NetworkConnectivityObserver) {
    val status by networkObserver.observe().collectAsState(initial = ConnectivityStatus.Available)
    val isOffline = status != ConnectivityStatus.Available

    AnimatedVisibility(
        visible = isOffline,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.WifiOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.no_internet),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                .align(Alignment.BottomCenter)
        )
        }
    }
}
