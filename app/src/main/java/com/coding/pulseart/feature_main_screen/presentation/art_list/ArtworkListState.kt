package com.coding.pulseart.feature_main_screen.presentation.art_list

import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

class ArtworkListState (
    val isLoading: Boolean = false,
    val artworks: List<ArtworkUi> = emptyList(),
    val selectedArtwork: ArtworkUi? = null
)