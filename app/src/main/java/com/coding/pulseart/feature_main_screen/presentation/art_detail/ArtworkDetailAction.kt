package com.coding.pulseart.feature_main_screen.presentation.art_detail

sealed interface ArtworkDetailAction {
    data class LoadArtworkDetail(val artworkId: String) : ArtworkDetailAction
}