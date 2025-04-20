package com.coding.pulseart.feature_main_screen.presentation.art_favorites

import com.coding.pulseart.core.domain.util.NetworkError

sealed interface FavouriteEvent {
    data class Error(val error: NetworkError): FavouriteEvent
}