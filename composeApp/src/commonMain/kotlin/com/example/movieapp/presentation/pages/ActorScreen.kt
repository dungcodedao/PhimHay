package com.example.movieapp.presentation.pages

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import coil3.compose.AsyncImage
import com.example.movieapp.R
import com.example.movieapp.presentation.components.ErrorState
import com.example.movieapp.presentation.components.TrendingMovieCard
import com.example.movieapp.presentation.viewmodel.ActorViewModel
import com.example.movieapp.ui.theme.AccentPurple
import com.example.movieapp.ui.theme.TextSecondary

@Composable
fun ActorScreen(
    onBackClick: () -> Unit,
    onMovieClick: (Int, Boolean) -> Unit,
    viewModel: ActorViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = AccentPurple) }
                uiState.error != null -> ErrorState(uiState.error!!, onRetry = { viewModel.retry() })
                uiState.actor != null -> {
                    val actor = uiState.actor!!
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        // Header Section
                        Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
                            // Background Blur Profile
                            AsyncImage(
                                model = actor.profileUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().blur(24.dp).alpha(0.3f)
                            )
                            
                            // Main Profile Image
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(220.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Brush.linearGradient(listOf(AccentPurple, Color.Cyan)), CircleShape)
                            ) {
                                AsyncImage(
                                    model = actor.profileUrl,
                                    contentDescription = actor.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Gradient Overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                                        )
                                    )
                            )
                            
                            // Name & Title
                            Column(
                                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = actor.name,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = actor.knownFor,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AccentPurple,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Info Section
                        Column(Modifier.padding(horizontal = 20.dp)) {
                            if (actor.birthday != null || actor.placeOfBirth != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    ActorInfoItem(label = stringResource(R.string.actor_birthday), value = actor.birthday ?: "--")
                                    ActorInfoItem(label = stringResource(R.string.actor_birthplace), value = actor.placeOfBirth ?: "--")
                                }
                                Spacer(Modifier.height(24.dp))
                            }

                            Text(stringResource(R.string.actor_biography), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = actor.biography.ifBlank { stringResource(R.string.actor_biography_empty) },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 24.sp
                            )
                            
                            Spacer(Modifier.height(32.dp))

                            // Movies Section
                            if (actor.movies.isNotEmpty()) {
                                Text(stringResource(R.string.actor_movies), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(16.dp))
                                LazyRow(
                                    contentPadding = PaddingValues(bottom = 40.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(actor.movies) { movie ->
                                        TrendingMovieCard(
                                            movie = movie,
                                            onMovieClick = { id, isTV ->
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                onMovieClick(id, isTV)
                                            },
                                            modifier = Modifier.width(260.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Top Bar Actions
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(Color.Black.copy(0.4f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = Color.White)
                }

                // Share Button (Optional but Premium)
                val context = LocalContext.current
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        uiState.actor?.let { actor ->
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TITLE, actor.name)
                                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_actor_message, actor.name))
                                }
                                context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_actor_title)))
                        }
                    },
                    modifier = Modifier
                        .background(Color.Black.copy(0.4f), CircleShape)
                ) {
                    Icon(Icons.Rounded.Share, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ActorInfoItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 12.sp, color = TextSecondary)
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
