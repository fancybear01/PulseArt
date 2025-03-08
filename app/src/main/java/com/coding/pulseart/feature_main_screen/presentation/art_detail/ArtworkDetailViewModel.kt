package com.coding.pulseart.feature_main_screen.presentation.art_detail

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.pulseart.core.domain.util.onError
import com.coding.pulseart.core.domain.util.onSuccess
import com.coding.pulseart.feature_main_screen.data.mappers.toArtworkUi
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListEvent
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class ArtworkDetailViewModel(
    private val artworkDataSource: ArtworkDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(ArtworkDetailState())
    val state = _state
    fun onAction(action: ArtworkDetailAction) {
        when (action) {
            is ArtworkDetailAction.LoadArtworkDetail -> getArtwork(action.artworkId)
        }
    }

    private val _events = Channel<ArtworkListEvent>()
    val events = _events.receiveAsFlow()

    private fun getArtwork(artworkId: String) {

        if (artworkId.isBlank()) {
            _state.update { it.copy(isError = true) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            Log.d(TAG, "getArtwork")
            artworkDataSource
                .getArtwork(artworkId)
                .onSuccess { artworkDetail ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            artworkDetail = artworkDetail
                        )
                    }
                }
                .onError {
                    Log.e(TAG, "getArtwork error: $it")
                    _state.update { it.copy(isLoading = false, isError = true) }
                    _events.send(ArtworkListEvent.Error(it))
                }
        }
    }
}