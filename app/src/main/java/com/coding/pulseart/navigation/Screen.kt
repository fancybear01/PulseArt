package com.coding.pulseart.navigation

sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object ArtworkList : Screen

    @kotlinx.serialization.Serializable
    data object Favourite : Screen

    @kotlinx.serialization.Serializable
    data object Settings : Screen

    @kotlinx.serialization.Serializable
    data class ArtworkDetails(val artworkId: String) : Screen
}