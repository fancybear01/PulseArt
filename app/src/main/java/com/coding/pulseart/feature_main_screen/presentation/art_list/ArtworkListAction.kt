package com.coding.pulseart.feature_main_screen.presentation.art_list

import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

sealed interface ArtworkListAction {
    data object LoadInitial : ArtworkListAction
    data object Paginate : ArtworkListAction
}