package com.coding.pulseart.feature_main_screen.presentation.art_list

import com.coding.pulseart.core.domain.util.NetworkError

sealed interface ArtworkListEvent {
    data class Error(val error: NetworkError): ArtworkListEvent
}