package com.coding.pulseart.feature_main_screen.presentation.art_search

import com.coding.pulseart.core.domain.util.NetworkError

sealed interface SearchEvent {
    data class Error(val error: NetworkError): SearchEvent
}