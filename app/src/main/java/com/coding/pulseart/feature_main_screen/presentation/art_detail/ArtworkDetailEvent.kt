package com.coding.pulseart.feature_main_screen.presentation.art_detail

import com.coding.pulseart.core.domain.util.NetworkError

sealed interface ArtworkDetailEvent {
    data class Error(val error: NetworkError): ArtworkDetailEvent
    data class UnknownError(val error: Throwable): ArtworkDetailEvent
    data object FavouriteStatusChanged: ArtworkDetailEvent
}