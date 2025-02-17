package com.coding.pulseart.feature_main_screen.presentation.art_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.pulseart.core.domain.util.onError
import com.coding.pulseart.core.domain.util.onSuccess
import com.coding.pulseart.feature_main_screen.data.mappers.toArtworkUi
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtListViewModel(
    private val artworkDataSource: ArtworkDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(ArtworkListState())
    val state = _state
        .onStart { loadArtworks() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(
                5000L
            ),
            ArtworkListState()
        )

    private val _events = Channel<ArtworkListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ArtworkListAction) {
        when (action) {
            is ArtworkListAction.OnArtworkClick -> {
                selectArtwork(action.artworkUi)
            }
        }
    }

    private fun selectArtwork(artworkUi: ArtworkUi) {
        _state.update { it.copy(selectedArtwork = artworkUi) }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedArtwork = it.selectedArtwork
                )
            }
        }
    }

    private suspend fun loadArtworks() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
        }

        artworkDataSource
            .getArtworks()
            .onSuccess { artworks ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        artworks = artworks.map { it.toArtworkUi() }
                    )
                }
            }
            .onError { error ->
                _state.update { it.copy(isLoading = false) }
                _events.send(ArtworkListEvent.Error(error))
            }
    }
}