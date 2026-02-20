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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem("Trang chủ", HomeRoute, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Tìm kiếm", SearchRoute, Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("Yêu thích", FavoriteRoute, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavItem("Cá nhân", ProfileRoute, Icons.Filled.Person, Icons.Outlined.Person),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 0.dp
            ) {
                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(item.route::class)
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label, fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        )
                    )
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
                    onMovieClick = { onNavigateToDetail(it) }
                )
            }

            composable<SearchRoute> {
                SearchScreen(
                    onMovieClick = { onNavigateToDetail(it) }
                )
            }

            composable<FavoriteRoute> {
                FavoriteScreen(
                    onMovieClick = { onNavigateToDetail(it) }
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
