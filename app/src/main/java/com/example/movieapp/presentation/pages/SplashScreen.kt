package com.example.movieapp.presentation.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.presentation.viewmodel.AuthViewModel
import com.example.movieapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: (Boolean) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // ──────────────────────────────────────────────────
    // ANIMATION STATES
    // ──────────────────────────────────────────────────
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(40f) }
    val letterSpacing = remember { Animatable(0f) }
    val glowAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1. Logo pop up
        launch {
            logoScale.animateTo(1f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
        }
        launch {
            logoAlpha.animateTo(1f, animationSpec = tween(800))
        }
        
        // 2. Text slide up + fade in
        delay(400)
        launch {
            textAlpha.animateTo(1f, animationSpec = tween(1000))
        }
        launch {
            textOffset.animateTo(0f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
        }
        
        // 3. Letter spacing expansion (Cinematic effect)
        launch {
            letterSpacing.animateTo(6f, animationSpec = tween(2000, easing = LinearOutSlowInEasing))
        }

        // 4. Background glow pulse
        launch {
            glowAlpha.animateTo(0.6f, animationSpec = tween(1500))
        }

        delay(2800)
        onSplashFinished(uiState.isLoggedIn)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030114)), // Deep cinematic black
        contentAlignment = Alignment.Center
    ) {
        // Decorative background glow
        Box(
            modifier = Modifier
                .size(400.dp)
                .drawBehind {
                    drawCircle(
                        Brush.radialGradient(
                            colors = listOf(AccentPurple.copy(glowAlpha.value * 0.4f), Color.Transparent),
                            center = center
                        )
                    )
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(logoScale.value)
                    .drawBehind {
                        // Vòng tròn sáng mờ phía sau
                        drawCircle(
                            Brush.sweepGradient(listOf(AccentPurple.copy(0.5f), AccentCyan.copy(0.5f), AccentPurple.copy(0.5f))),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                        )
                    }
                    .padding(6.dp)
                    .background(
                        Brush.linearGradient(listOf(AccentPurple, AccentCyan)),
                        RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "▶",
                    fontSize = 42.sp,
                    color = Color.White,
                    modifier = Modifier.offset(x = 2.dp) // Căn chỉnh biểu tượng play
                )
            }

            Spacer(Modifier.height(32.dp))

            // Brand Name
            Text(
                text = "PhimHay",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = letterSpacing.value.sp,
                modifier = Modifier
                    .graphicsLayer(
                        alpha = textAlpha.value,
                        translationY = textOffset.value
                    )
            )

            // Slogan
            Text(
                text = "CINEMATIC EXPERIENCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AccentCyan.copy(alpha = textAlpha.value),
                letterSpacing = (letterSpacing.value * 0.5f).sp,
                modifier = Modifier
                    .graphicsLayer(
                        alpha = textAlpha.value * 0.8f,
                        translationY = textOffset.value * 0.5f
                    )
            )
        }

        // Loading bar at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .width(120.dp)
                    .height(2.dp)
                    .graphicsLayer(alpha = textAlpha.value * 0.5f),
                color = AccentPurple,
                trackColor = Color.White.copy(0.1f)
            )
        }
    }
}
