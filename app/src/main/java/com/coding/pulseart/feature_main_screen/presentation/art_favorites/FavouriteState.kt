package com.coding.pulseart.feature_main_screen.presentation.art_favorites

import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

data class FavouriteState(
    val isLoading: Boolean = false,
    val artworks: List<ArtworkUi> = emptyList()
)