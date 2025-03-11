package com.coding.pulseart.feature_main_screen.presentation.art_detail

import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail

data class ArtworkDetailState(
    val artworkDetail: ArtworkDetail? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isFavorite: Boolean = false
)