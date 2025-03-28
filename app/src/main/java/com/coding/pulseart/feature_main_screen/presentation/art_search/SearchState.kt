package com.coding.pulseart.feature_main_screen.presentation.art_search

import androidx.compose.runtime.Immutable
import com.coding.pulseart.feature_main_screen.domain.Artwork
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi


data class SearchState (
    val searchQuery: String = "",
    val results: List<Artwork> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)