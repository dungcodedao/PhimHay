package com.example.movieapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable object SplashRoute
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object HomeRoute
@Serializable data class DetailRoute(val movieId: Int)
@Serializable object SearchRoute
@Serializable object FavoriteRoute
@Serializable object ProfileRoute
@Serializable object MainRoute
@Serializable data class ActorRoute(val actorId: Int)
@Serializable object HistoryRoute
@Serializable data class VideoPlayerRoute(val url: String, val title: String)
