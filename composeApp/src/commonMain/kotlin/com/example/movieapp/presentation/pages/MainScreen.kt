package com.example.movieapp.presentation.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.movieapp.core.util.ResStrings
import com.example.movieapp.ui.theme.AccentPurple
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.presentation.navigation.FavoriteRoute
import com.example.movieapp.presentation.navigation.HomeRoute
import com.example.movieapp.presentation.navigation.ProfileRoute
import com.example.movieapp.presentation.navigation.SearchRoute

data class BottomNavItem(
    val label: String,
    val route: Any,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainScreen(
    onNavigateToDetail: (Int, Boolean) -> Unit,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val haptic = LocalHapticFeedback.current

    val bottomNavItems = listOf(
        BottomNavItem(ResStrings.nav_home, HomeRoute, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(ResStrings.nav_search, SearchRoute, Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem(ResStrings.nav_favorite, FavoriteRoute, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavItem(ResStrings.nav_profile, ProfileRoute, Icons.Filled.Person, Icons.Outlined.Person),
    )

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .height(72.dp)
            ) {
                // Glassmorphism background layer
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                                )
                            )
                        )
                        .blur(15.dp) // Blur the background only
                )

                // Foreground NavigationBar with sharp icons
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    tint = if (selected) AccentPurple else MaterialTheme.colorScheme.onSurface.copy(0.6f)
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) AccentPurple else MaterialTheme.colorScheme.onSurface.copy(0.6f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = AccentPurple.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { 100 }) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    onMovieClick = { id, isTV -> onNavigateToDetail(id, isTV) }
                )
            }

            composable<SearchRoute> {
                SearchScreen(
                    onMovieClick = { id, isTV -> onNavigateToDetail(id, isTV) }
                )
            }

            composable<FavoriteRoute> {
                FavoriteScreen(
                    onMovieClick = { id, isTV -> onNavigateToDetail(id, isTV) }
                )
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    onNavigateLogin = onNavigateToLogin,
                    onNavigateHistory = onNavigateToHistory,
                    onNavigateFavorite = {
                        navController.navigate(FavoriteRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
