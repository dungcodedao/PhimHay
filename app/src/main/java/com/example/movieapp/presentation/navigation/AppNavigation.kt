package com.example.movieapp.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movieapp.*
import com.example.movieapp.presentation.pages.*

import androidx.navigation.navDeepLink

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = SplashRoute
        ) {
            // Splash
            composable<SplashRoute> {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(MainRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }
                )
            }

            // Main (có Bottom Nav bên trong)
            composable<MainRoute> {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalAnimatedContentScope provides this@composable
                ) {
                    MainScreen(
                        onNavigateToDetail = { movieId ->
                            navController.navigate(DetailRoute(movieId = movieId))
                        },
                        onNavigateToLogin = {
                            navController.navigate(LoginRoute)
                        },
                        onNavigateToHistory = {
                            navController.navigate(HistoryRoute)
                        }
                    )
                }
            }

            // Detail
            composable<DetailRoute>(
                deepLinks = listOf(
                    navDeepLink { uriPattern = "movieapp://movie/{movieId}" },
                    navDeepLink { uriPattern = "https://movieapp.com/movie/{movieId}" }
                )
            ) {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalAnimatedContentScope provides this@composable
                ) {
                    DetailScreen(
                        onBackClick = { navController.popBackStack() },
                        onMovieClick = { movieId ->
                            navController.navigate(DetailRoute(movieId = movieId))
                        },
                        onCastClick = { actorId ->
                            navController.navigate(ActorRoute(actorId = actorId))
                        },
                        onPlayClick = { url, title ->
                            navController.navigate(VideoPlayerRoute(url = url, title = title))
                        }
                    )
                }
            }

            // Video Player
            composable<VideoPlayerRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<VideoPlayerRoute>()
                VideoPlayerScreen(
                    url = route.url,
                    title = route.title,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Actor Detail
            composable<ActorRoute> {
                ActorScreen(
                    onBackClick = { navController.popBackStack() },
                    onMovieClick = { movieId ->
                        navController.navigate(DetailRoute(movieId = movieId))
                    }
                )
            }

            // History
            composable<HistoryRoute> { backStackEntry ->
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalAnimatedContentScope provides this@composable
                ) {
                    HistoryScreen(
                        onMovieClick = { movieId ->
                            navController.navigate(DetailRoute(movieId = movieId))
                        }
                    )
                }
            }

            // Login
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(MainRoute) {
                                popUpTo(LoginRoute) { inclusive = true }
                            }
                        }
                    },
                    onNavigateRegister = {
                        navController.navigate(RegisterRoute)
                    }
                )
            }

            // Register
            composable<RegisterRoute> {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(MainRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
