package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.presentation.viewmodel.AuthViewModel
import com.example.movieapp.presentation.viewmodel.FavoriteViewModel
import com.example.movieapp.presentation.viewmodel.HistoryViewModel
import com.example.movieapp.presentation.viewmodel.ThemeViewModel
import com.example.movieapp.ui.theme.*

@Composable
fun ProfileScreen(
    onNavigateLogin: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateFavorite: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()
    val favoriteState by favoriteViewModel.uiState.collectAsState()
    val historyState by historyViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bytes = inputStream?.readBytes()
            bytes?.let { b -> viewModel.uploadAvatar(b) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding() // Thêm padding ở dưới cùng để không bị che bởi thanh điều hướng
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ──────────────────────────────────────────────────
        // HEADER BANNER - Sửa lỗi tràn viền
        // ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Tăng chiều cao thêm để không bị chật
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A0A3E),
                            Color(0xFF030114)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Avatar với vòng gradient
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            Brush.sweepGradient(listOf(AccentPurple, AccentCyan, AccentPurple)),
                            CircleShape
                        )
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF0D0D1A))
                            .clickable(enabled = uiState.isLoggedIn) { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.avatarUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(uiState.avatarUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Rounded.Person, null,
                                modifier = Modifier.size(52.dp),
                                tint = AccentPurple.copy(0.8f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (uiState.isLoggedIn) {
                    Text(
                        text = uiState.username ?: uiState.userEmail?.substringBefore("@") ?: "Người dùng",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        uiState.userEmail ?: "",
                        fontSize = 13.sp,
                        color = TextSecondary.copy(0.8f)
                    )
                    Spacer(Modifier.height(12.dp))
                    
                    // Badge Premium
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = AccentGold.copy(0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Rounded.Star, null, tint = AccentGold, modifier = Modifier.size(14.dp))
                            Text("PREMIUM MEMBER", color = AccentGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    // Thêm khoảng trống ở dưới cùng của header để Stats Card không đè lên
                    Spacer(Modifier.height(30.dp))
                } else {
                    Text("Ready to Watch? 🎬", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("Tham gia ngay để lưu phim và nhận thông báo!", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(20.dp))
                }
            }
        }

        // ──────────────────────────────────────────────────
        // STATS & CTA BAR
        // ──────────────────────────────────────────────────
        if (uiState.isLoggedIn) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp), // Giảm bớt độ đẩy lên để không đè Badge
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF141426),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.05f)),
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileStatItem(
                        value = historyState.history.size.toString(),
                        label = "ĐÃ XEM",
                        color = AccentCyan
                    )
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(0.1f)))
                    ProfileStatItem(
                        value = favoriteState.favorites.size.toString(),
                        label = "YÊU THÍCH",
                        color = ErrorRed
                    )
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(0.1f)))
                    ProfileStatItem(
                        value = "1.2k",
                        label = "PHÚT XEM",
                        color = AccentGold
                    )
                }
            }
        } else {
            Button(
                onClick = onNavigateLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp)
                    .offset(y = (-24).dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Rounded.Login, null)
                    Text("ĐĂNG NHẬP NGAY", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                }
            }
        }

        Spacer(Modifier.height(if (uiState.isLoggedIn) 0.dp else 8.dp))

        // ──────────────────────────────────────────────────
        // MENU ITEMS nhóm theo card
        // ──────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nhóm 1: Hoạt động
            MenuGroupCard {
                ProfileMenuRow(
                    icon = Icons.Rounded.History,
                    iconBg = AccentCyan.copy(0.15f),
                    iconTint = AccentCyan,
                    label = "Lịch sử xem",
                    badge = if (historyState.history.isNotEmpty()) historyState.history.size.toString() else null,
                    onClick = onNavigateHistory
                )
                HorizontalDivider(color = Color.White.copy(0.06f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuRow(
                    icon = Icons.Rounded.Favorite,
                    iconBg = ErrorRed.copy(0.15f),
                    iconTint = ErrorRed,
                    label = "Phim đã thích",
                    badge = if (favoriteState.favorites.isNotEmpty()) favoriteState.favorites.size.toString() else null,
                    onClick = onNavigateFavorite
                )
            }

            // Nhóm 2: Cài đặt
            MenuGroupCard {
                ProfileMenuRow(
                    icon = if (isDarkMode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                    iconBg = AccentGold.copy(0.15f),
                    iconTint = AccentGold,
                    label = if (isDarkMode) "Chế độ tối" else "Chế độ sáng",
                    trailing = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { themeViewModel.toggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentPurple
                            )
                        )
                    }
                )
                HorizontalDivider(color = Color.White.copy(0.06f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuRow(
                    icon = Icons.Rounded.Settings,
                    iconBg = TextSecondary.copy(0.15f),
                    iconTint = TextSecondary,
                    label = "Cài đặt tài khoản",
                    onClick = {}
                )
                HorizontalDivider(color = Color.White.copy(0.06f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuRow(
                    icon = Icons.Rounded.Info,
                    iconBg = AccentCyan.copy(0.1f),
                    iconTint = AccentCyan,
                    label = "Về ứng dụng",
                    subtitle = "Phiên bản 1.0.0",
                    onClick = {}
                )
            }

            // Nhóm 3: Đăng xuất
            if (uiState.isLoggedIn) {
                MenuGroupCard {
                    ProfileMenuRow(
                        icon = Icons.Rounded.Logout,
                        iconBg = ErrorRed.copy(0.15f),
                        iconTint = ErrorRed,
                        label = "Đăng xuất",
                        labelColor = ErrorRed,
                        showChevron = false,
                        onClick = { viewModel.logout() }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────
// COMPONENTS
// ──────────────────────────────────────────────────

@Composable
private fun ProfileStatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun MenuGroupCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1C1C2E)
    ) {
        Column(content = content)
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    subtitle: String? = null,
    labelColor: Color = Color.White,
    badge: String? = null,
    showChevron: Boolean = true,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Icon với nền tròn
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = labelColor)
            if (subtitle != null) {
                Text(subtitle, fontSize = 11.sp, color = TextSecondary)
            }
        }

        // Badge số
        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AccentPurple.copy(0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(badge, fontSize = 12.sp, color = AccentPurple, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
        }

        if (trailing != null) {
            trailing()
        } else if (showChevron) {
            Icon(Icons.Rounded.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        }
    }
}
