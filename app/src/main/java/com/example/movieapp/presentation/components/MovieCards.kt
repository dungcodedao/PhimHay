package com.example.movieapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.ui.theme.AccentGold
import androidx.compose.animation.ExperimentalSharedTransitionApi
import com.example.movieapp.presentation.navigation.LocalAnimatedContentScope
import com.example.movieapp.presentation.navigation.LocalSharedTransitionScope

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TrendingMovieCard(
    movie: Movie,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    Card(
        modifier = modifier.width(300.dp).height(180.dp)
            .clickable { onMovieClick(movie.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.backdropUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop, 
                modifier = Modifier.fillMaxSize()
                    .run {
                        if (sharedTransitionScope != null && animatedContentScope != null) {
                            with(sharedTransitionScope) {
                                this@run.sharedElement(
                                    rememberSharedContentState(key = "movie_backdrop_${movie.id}"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                            }
                        } else this
                    }
            )
            Box(Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                    startY = 80f
                )
            ))
            Box(
                Modifier.align(Alignment.TopEnd).padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Star, null, tint = AccentGold, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(String.format("%.1f", movie.voteAverage),
                        color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White, fontWeight = FontWeight.Bold,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieGridCard(
    movie: Movie,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    Column(modifier = modifier.clickable { onMovieClick(movie.id) }.padding(bottom = 16.dp)) {
        Card(shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .run {
                            if (sharedTransitionScope != null && animatedContentScope != null) {
                                with(sharedTransitionScope) {
                                    this@run.sharedElement(
                                        rememberSharedContentState(key = "movie_poster_${movie.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                }
                            } else this
                        }
                )
                Box(
                    Modifier.align(Alignment.TopEnd).padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Star, null, tint = AccentGold, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(3.dp))
                        Text(String.format("%.1f", movie.voteAverage),
                            color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(movie.title, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(movie.releaseDate.take(4), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.ExtraBold
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
