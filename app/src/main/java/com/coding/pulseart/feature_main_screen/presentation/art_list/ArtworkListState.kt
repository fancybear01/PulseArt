package com.coding.pulseart.feature_main_screen.presentation.art_list

import androidx.compose.runtime.Immutable
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

@Immutable
data class ArtworkListState (
    val isLoading: Boolean = false,
    val artworks: List<ArtworkUi> = emptyList(),
    val selectedArtwork: ArtworkUi? = null,
    val nextPage: Int? = null,
    val isEndReached: Boolean = false,
    val totalPages: Int = 1411,
)