package com.coding.pulseart.feature_main_screen.presentation.art_favorites

sealed interface FavouriteAction {
    data object LoadInitial: FavouriteAction
}