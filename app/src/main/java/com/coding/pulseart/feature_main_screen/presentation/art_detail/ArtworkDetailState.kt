package com.coding.pulseart.feature_main_screen.presentation.art_detail

import com.coding.pulseart.feature_main_screen.domain.Artwork

data class ArtworkDetailState(
    val artwork: Artwork? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)