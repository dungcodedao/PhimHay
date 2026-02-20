package com.example.movieapp.presentation.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movieapp.presentation.components.ErrorState
import com.example.movieapp.presentation.viewmodel.ActorViewModel

@Composable
fun ActorScreen(
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: ActorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                uiState.error != null -> ErrorState(uiState.error!!, onRetry = { viewModel.retry() })
                uiState.actor != null -> {
                    val actor = uiState.actor!!
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        AsyncImage(
                            model = actor.profileUrl,
                            contentDescription = actor.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(400.dp)
                        )
                        Column(Modifier.padding(20.dp)) {
                            Text(actor.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(actor.biography, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            IconButton(onClick = onBackClick, modifier = Modifier.padding(12.dp).background(Color.Black.copy(0.4f), CircleShape)) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = Color.White)
            }
        }
    }
}
