package com.coding.pulseart.feature_main_screen.presentation.art_detail

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.pulseart.core.domain.util.onError
import com.coding.pulseart.core.domain.util.onSuccess
import com.coding.pulseart.feature_main_screen.data.local.FavouriteDao
import com.coding.pulseart.feature_main_screen.data.mappers.toFavouriteEntity
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkDetail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtworkDetailViewModel(
    private val artworkDataSource: ArtworkDataSource,
    private val dao: FavouriteDao
) : ViewModel() {
    private val _state = MutableStateFlow(ArtworkDetailState())
    val state = _state

    fun onAction(action: ArtworkDetailAction) {
        when (action) {
            is ArtworkDetailAction.LoadArtworkDetail -> getArtwork(action.artworkId)
        }
    }

    private val _events = Channel<ArtworkDetailEvent>()
    val events = _events.receiveAsFlow()

    fun checkFavoriteStatus(artworkId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isFavorite = dao.isFavorite(artworkId)) }
        }
    }

    fun toggleFavorite(artworkDetail: ArtworkDetail?) {
        viewModelScope.launch {
            artworkDetail?.let {
                if (_state.value.isFavorite) {
                    dao.delete(artworkDetail.toFavouriteEntity())
                } else {
                    dao.insert(artworkDetail.toFavouriteEntity())
                }
                _state.update { it.copy(isFavorite = !it.isFavorite) }
            } ?: run {
                _events.send(ArtworkDetailEvent.UnknownError(Exception("UnknownError")))
            }
        }
    }

    private fun getArtwork(artworkId: String) {

        if (artworkId.isBlank()) {
            _state.update { it.copy(isError = true) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            artworkDataSource
                .getArtwork(artworkId)
                .onSuccess { artworkDetail ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            artworkDetail = artworkDetail
                        )
                    }
                    checkFavoriteStatus(artworkId)
                }
                .onError {
                    Log.e(TAG, "getArtwork error: $it")
                    _state.update { it.copy(isLoading = false, isError = true) }
                    _events.send(ArtworkDetailEvent.Error(it))
                }
        }
    }
}