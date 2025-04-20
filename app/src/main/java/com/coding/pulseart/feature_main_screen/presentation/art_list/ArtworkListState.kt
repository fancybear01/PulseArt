package com.coding.pulseart.feature_main_screen.presentation.art_list

import androidx.compose.runtime.Immutable
import com.coding.pulseart.feature_main_screen.presentation.models.Artwork

@Immutable
data class ArtworkListState (
    val isLoading: Boolean = false,
    val artworks: List<Artwork> = emptyList(),
    val cache: Map<ArtworkFilterType, List<Artwork>> = emptyMap(),
    val selectedArtwork: Artwork? = null,
    val nextPage: Int? = null,
    val isEndReached: Boolean = false,
    val totalPages: Int = 1411,
    val selectedFilter: ArtworkFilterType = ArtworkFilterType.All,
    val shouldResetPagination: Boolean = false
)