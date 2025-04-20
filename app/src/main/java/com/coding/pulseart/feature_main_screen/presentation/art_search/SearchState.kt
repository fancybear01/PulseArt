package com.coding.pulseart.feature_main_screen.presentation.art_search

import com.coding.pulseart.feature_main_screen.presentation.models.SearchItem


data class SearchState (
    val searchQuery: String = "",
    var results: List<SearchItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)