package com.example.movieapp.presentation.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.movieapp.R

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    LottieStateScreen(
        animationRes = R.raw.error_state,
        title = "Oops! Đã có lỗi xảy ra",
        subtitle = message,
        titleColor = MaterialTheme.colorScheme.error,
        actionLabel = "Thử lại",
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    @RawRes animationRes: Int = R.raw.empty_state,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LottieStateScreen(
        animationRes = animationRes,
        title = title,
        subtitle = subtitle,
        actionLabel = actionLabel,
        onAction = onAction,
        modifier = modifier
    )
}

@Composable
private fun LottieStateScreen(
    @RawRes animationRes: Int,
    title: String,
    subtitle: String,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onBackground,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRes))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(200.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onAction,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(actionLabel, fontWeight = FontWeight.Bold)
            }
        }
    }
}
