package com.coding.pulseart.feature_main_screen.presentation.art_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import kotlinx.coroutines.launch

class ArtworkDetailViewModel(
    private val artworkDataSource: ArtworkDataSource
) : ViewModel() {
    var state by mutableStateOf(ArtworkDetailState())
        private set

    fun onAction(action: ArtworkDetailAction) {
        when (action) {
            is ArtworkDetailAction.LoadArtworkDetail -> getArtwork(action.artworkId)
        }
    }

    private fun getArtwork(artworkId: String) {

        if (artworkId.isBlank()) {
            state = state.copy(
                isError = true
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            artworkDataSource.getArtwork(artworkId)
        }
    }
}