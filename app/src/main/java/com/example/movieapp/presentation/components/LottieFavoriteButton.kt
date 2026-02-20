package com.example.movieapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun LottieFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://lottie.host/80401d4a-d686-42d7-b866-267576a8a474/e0SjX7Qk5Y.json")
    )
    
    // Manage animation progress based on favorite state
    var isPlaying by remember { mutableStateOf(false) }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        speed = 1.5f,
        iterations = 1 // Play once
    )

    // Sync animation progress with state
    LaunchedEffect(isFavorite) {
        if (isFavorite) {
            isPlaying = true
        }
    }

    Box(
        modifier = modifier
            .size(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPlaying = !isFavorite // Trigger animation if becoming favorite
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { if (isFavorite) progress else 0f },
            modifier = Modifier.size(70.dp) // Lottie usually has transparent padding
        )
    }
}
