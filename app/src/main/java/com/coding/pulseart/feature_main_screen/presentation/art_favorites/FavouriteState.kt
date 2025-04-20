package com.coding.pulseart.feature_main_screen.presentation.art_favorites

import com.coding.pulseart.feature_main_screen.presentation.models.Artwork

data class FavouriteState(
    val isLoading: Boolean = false,
    val artworks: List<Artwork> = emptyList()
)