package com.example.movieapp.presentation.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.movieapp.R
import com.example.movieapp.domain.model.Cast
import com.example.movieapp.domain.model.Rating
import com.example.movieapp.presentation.components.ErrorState
import com.example.movieapp.presentation.components.LottieFavoriteButton
import com.example.movieapp.presentation.components.ShimmerDetail
import com.example.movieapp.presentation.navigation.LocalAnimatedContentScope
import com.example.movieapp.presentation.navigation.LocalSharedTransitionScope
import com.example.movieapp.presentation.viewmodel.DetailViewModel
import com.example.movieapp.ui.theme.AccentGold

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onPlayClick: (String, String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    if (uiState.isLoading && uiState.movieDetail == null) {
        ShimmerDetail()
    } else if (uiState.error != null && uiState.movieDetail == null) {
        ErrorState(message = uiState.error!!, onRetry = { viewModel.retry() })
    } else {
        uiState.movieDetail?.let { movie ->
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                // Main Content
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                    Box(modifier = Modifier.fillMaxWidth().height(500.dp)) {
                        with(sharedTransitionScope!!) {
                            AsyncImage(
                                model = movie.posterUrl,
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        rememberSharedContentState(key = "movie_poster_${movie.id}"),
                                        animatedVisibilityScope = animatedContentScope!!
                                    )
                            )
                        }
                        
                        val bgColor = MaterialTheme.colorScheme.background
                        val gradientColor = uiState.extractedColors["vibrant"] ?: uiState.extractedColors["darkVibrant"] ?: bgColor

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            gradientColor.copy(alpha = 0.5f),
                                            bgColor
                                        ),
                                        startY = 100f
                                    )
                                )
                        )
                    }

                    Column(modifier = Modifier.offset(y = (-40).dp)) {
                        // Title and Stats
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Star, null, tint = AccentGold, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "${movie.voteAverage}/10",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(movie.releaseDate, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.width(12.dp))
                                Text(stringResource(R.string.runtime_min, movie.runtime), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Genres
                        FlowRow(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            movie.genres.forEach { genre ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(genre.name) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        labelColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = null,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nút "Xem Phim" → Dùng VidSrc (nguồn stream phim thực sự, không bị chặn!)
                            Button(
                                onClick = {
                                    onPlayClick("https://vidsrc.to/embed/movie/${movie.id}", movie.title)
                                },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Rounded.PlayArrow, null)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.watch_now), fontWeight = FontWeight.ExtraBold)
                            }

                            // Nút "Trailer" → Dùng YouTube (chỉ cho xem trailer, dùng nút mở trình duyệt nếu cần)
                            val trailer = uiState.videos.firstOrNull { it.isYoutubeTrailer }
                            FilledTonalIconButton(
                                onClick = {
                                    trailer?.let {
                                        onPlayClick("https://www.youtube-nocookie.com/embed/${it.key}?rel=0&autoplay=1", "Trailer: ${movie.title}")
                                    }
                                },
                                modifier = Modifier.size(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled = trailer != null,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.PlayCircle, 
                                    contentDescription = "Trailer",
                                    tint = if (trailer != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val shareTitle = stringResource(R.string.share_title)
                                val shareMessage = stringResource(R.string.share_message, movie.title, movie.id)
                                IconButton(onClick = {
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TITLE, movie.title)
                                        putExtra(Intent.EXTRA_TEXT, shareMessage)
                                    }
                                    context.startActivity(Intent.createChooser(intent, shareTitle))
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                LottieFavoriteButton(
                                    isFavorite = uiState.isFavorite,
                                    onClick = { viewModel.toggleFavorite() }
                                )
                            }
                        }

                        // Movie Overview
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(stringResource(R.string.overview_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Cast Section
                        if (uiState.cast.isNotEmpty()) {
                            Text(
                                stringResource(R.string.cast_title),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(uiState.cast) { actor ->
                                    CastItem(actor, onClick = { onCastClick(actor.id) })
                                }
                            }
                            Spacer(Modifier.height(24.dp))
                        }

                        // ⭐ Rating Section
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        stringResource(R.string.rating_title),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (uiState.ratings.isNotEmpty()) {
                                        Text(
                                            stringResource(R.string.review_count, uiState.ratings.size),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                var showRatingDialog by remember { mutableStateOf(false) }
                                if (uiState.userRating == null) {
                                    TextButton(onClick = { showRatingDialog = true }) {
                                        Icon(Icons.Rounded.Edit, null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(stringResource(R.string.write_review))
                                    }
                                }

                                if (showRatingDialog) {
                                    RatingDialog(
                                        onDismiss = { showRatingDialog = false },
                                        onSubmit = { score, comment ->
                                            viewModel.submitRating(score, comment)
                                            showRatingDialog = false
                                        },
                                        isSubmitting = uiState.isSubmittingRating
                                    )
                                }
                            }

                            if (uiState.ratings.isEmpty()) {
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    stringResource(R.string.no_ratings),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Spacer(Modifier.height(16.dp))
                                uiState.ratings.take(3).forEach { rating ->
                                    RatingItem(rating)
                                    Spacer(Modifier.height(12.dp))
                                }
                            }
                        }

                        // Similar Movies Section
                        if (uiState.similarMovies.isNotEmpty()) {
                            Text(
                                stringResource(R.string.similar_movies),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                            )
                            LazyRow(
                                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.similarMovies) { similar ->
                                    Card(
                                        onClick = { onMovieClick(similar.id) },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.width(110.dp).height(160.dp)
                                    ) {
                                        AsyncImage(
                                            model = similar.posterUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(0.4f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun CastItem(cast: Cast, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp).clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = cast.profileUrl,
            contentDescription = cast.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            cast.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            cast.character,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun RatingItem(rating: Rating) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = rating.userAvatar ?: R.drawable.ic_launcher_foreground,
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(rating.userName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating.rating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (index < rating.rating) AccentGold else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                rating.comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RatingDialog(
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit,
    isSubmitting: Boolean
) {
    var score by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.write_review),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(Modifier.height(20.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(5) { index ->
                        val currentScore = index + 1
                        IconButton(onClick = { score = currentScore }) {
                            Icon(
                                imageVector = if (score >= currentScore) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = if (score >= currentScore) AccentGold else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
                
                Spacer(Modifier.height(20.dp))
                
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text(stringResource(R.string.write_review)) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                
                Spacer(Modifier.height(24.dp))
                
                Button(
                    onClick = { onSubmit(score, comment) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isSubmitting && comment.isNotBlank()
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(stringResource(R.string.submit_rating))
                    }
                }
            }
        }
    }
}
