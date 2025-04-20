package com.coding.pulseart.feature_main_screen.presentation.art_favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.pulseart.feature_main_screen.data.local.FavouriteDao
import com.coding.pulseart.feature_main_screen.data.mappers.toArtwork
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val dao: FavouriteDao
) : ViewModel() {

    private val _state = MutableStateFlow(FavouriteState())
    val state = _state.asStateFlow()

    private val _events = Channel<ArtworkListEvent>()
    val events = _events.receiveAsFlow()

    init {
        onAction(FavouriteAction.LoadInitial)
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            dao.getAllFavorites()
                .map { favorites ->
                    favorites.map { it.toArtwork() }
                }
                .collect { artworks ->
                    _state.update {
                        it.copy(
                            artworks = artworks,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onAction(action: FavouriteAction) {
        when (action) {
            FavouriteAction.LoadInitial -> loadFavorites()
        }
    }
}