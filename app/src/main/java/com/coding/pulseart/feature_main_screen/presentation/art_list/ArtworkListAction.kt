package com.coding.pulseart.feature_main_screen.presentation.art_list

sealed interface ArtworkListAction {
    data object LoadInitial : ArtworkListAction
    data object Paginate : ArtworkListAction
    data class FilterChanged(val filter: ArtworkFilterType) : ArtworkListAction
}