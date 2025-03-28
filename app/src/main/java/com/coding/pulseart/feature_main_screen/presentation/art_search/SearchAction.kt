package com.coding.pulseart.feature_main_screen.presentation.art_search

sealed interface SearchAction {
    data object SearchArtworks : SearchAction
}