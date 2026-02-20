package com.example.movieapp.presentation.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    // Animation scale cho logo
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }
    // Glow pulse animation
    val glowScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Hiệu ứng xuất hiện
        launch {
            scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        }
        launch {
            alpha.animateTo(1f, animationSpec = tween(600))
        }
        // Pulse glow liên tục
        launch {
            while (true) {
                glowScale.animateTo(1.15f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
                glowScale.animateTo(1f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
            }
        }
        delay(2500)
        onSplashFinished(uiState.isLoggedIn)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A0A3E),
                        Color(0xFF0D0D0D)
                    ),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glow nền phía sau logo
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(glowScale.value)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentPurple.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
                .blur(40.dp)
        )

        Column(
            modifier = Modifier.scale(scale.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon logo chữ P
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AccentPurple, AccentCyan)
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "▶",
                    fontSize = 38.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "PhimHay",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = "Thế giới điện ảnh trong tầm tay",
                fontSize = 14.sp,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
        }

        // Loading indicator dưới cùng
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            LinearProgressIndicator(
                modifier = Modifier.width(80.dp),
                color = AccentPurple,
                trackColor = AccentPurple.copy(alpha = 0.2f)
            )
        }
    }
}
