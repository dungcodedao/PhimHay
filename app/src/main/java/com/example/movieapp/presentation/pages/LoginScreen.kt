package com.example.movieapp.presentation.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.presentation.viewmodel.AuthViewModel
import com.example.movieapp.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
            viewModel.clearState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0D1A),
                        Color(0xFF0D0D0D)
                    )
                )
            )
    ) {
        // Decorative orb top-left
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-80).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentPurple.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )
        // Decorative orb bottom-right
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentCyan.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(60.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AccentPurple, AccentCyan)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("▶", fontSize = 30.sp, color = Color.White)
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "Chào mừng trở lại",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Tiếp tục khám phá thế giới điện ảnh",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            // Card chứa form
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1C1C2E),
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email field
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Email", fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("your@email.com", color = TextSecondary.copy(0.5f)) },
                            leadingIcon = {
                                Icon(Icons.Rounded.Email, null, tint = if (isEmailValid) AccentCyan else TextSecondary)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            isError = email.isNotEmpty() && !isEmailValid,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentPurple,
                                unfocusedBorderColor = Color.White.copy(0.1f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentPurple,
                                errorBorderColor = ErrorRed
                            )
                        )
                    }

                    // Password field
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Mật khẩu", fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••", color = TextSecondary.copy(0.5f)) },
                            leadingIcon = {
                                Icon(Icons.Rounded.Lock, null, tint = if (isPasswordValid) AccentCyan else TextSecondary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                                        null,
                                        tint = TextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            isError = password.isNotEmpty() && !isPasswordValid,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentPurple,
                                unfocusedBorderColor = Color.White.copy(0.1f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AccentPurple,
                                errorBorderColor = ErrorRed
                            )
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Login button with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (!uiState.isLoading && isEmailValid && isPasswordValid)
                                    Brush.horizontalGradient(listOf(AccentPurple, Color(0xFF9B59B6)))
                                else
                                    Brush.horizontalGradient(listOf(Color(0xFF3A3A3A), Color(0xFF3A3A3A)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { viewModel.login(email, password) },
                            modifier = Modifier.fillMaxSize(),
                            enabled = !uiState.isLoading && isEmailValid && isPasswordValid,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Đăng nhập",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Error message
            if (uiState.error != null) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ErrorRed.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠ ${uiState.error}",
                        color = ErrorRed,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.1f))
                Text("  hoặc  ", color = TextSecondary, fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.1f))
            }

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(0.05f))
            ) {
                Text(
                    "Chưa có tài khoản? ",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    "Đăng ký ngay",
                    color = AccentCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
