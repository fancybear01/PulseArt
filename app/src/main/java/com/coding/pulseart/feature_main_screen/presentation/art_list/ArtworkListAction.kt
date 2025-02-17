package com.coding.pulseart.feature_main_screen.presentation.art_list

import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

sealed interface ArtworkListAction {
    data class OnArtworkClick(val artworkUi: ArtworkUi): ArtworkListAction
}