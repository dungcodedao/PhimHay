package com.example.movieapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerItem(modifier: Modifier) {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(modifier = modifier.background(brush))
}

@Composable
fun ShimmerCarousel() {
    Column {
        Box(Modifier.padding(16.dp).width(150.dp).height(24.dp).clip(RoundedCornerShape(4.dp)).background(Color.LightGray.copy(0.3f)))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) {
                ShimmerItem(Modifier.width(300.dp).height(180.dp).clip(RoundedCornerShape(16.dp)))
            }
        }
    }
}

@Composable
fun ShimmerGrid() {
    Column(modifier = Modifier.padding(16.dp)) {
        Box(Modifier.width(150.dp).height(24.dp).clip(RoundedCornerShape(4.dp)).background(Color.LightGray.copy(0.3f)))
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(2) {
                Column(modifier = Modifier.weight(1f)) {
                    ShimmerItem(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(12.dp)))
                    Spacer(Modifier.height(8.dp))
                    ShimmerItem(Modifier.fillMaxWidth(0.7f).height(16.dp).clip(RoundedCornerShape(4.dp)))
                    Spacer(Modifier.height(4.dp))
                    ShimmerItem(Modifier.fillMaxWidth(0.4f).height(12.dp).clip(RoundedCornerShape(4.dp)))
                }
            }
        }
    }
}
