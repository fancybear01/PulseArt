package com.coding.pulseart.feature_main_screen.presentation.art_list

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
            is ArtworkListAction.LoadInitial -> {
                if (state.value.artworks.isEmpty()) {
                    loadArtworks(page = 1)
                }
            }
            is ArtworkListAction.Paginate -> {
                if (
                    !state.value.isLoading &&
                    !state.value.isEndReached
                    ) {
                    loadArtworks()
                }
            }
        }
    }


    private fun loadArtworks(page: Int? = null) {
        val targetPage = page ?: _state.value.nextPage
        if (state.value.isLoading ||
            (targetPage != null && targetPage > 1 && state.value.isEndReached)) return
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    artworks = if (page == 1) emptyList() else it.artworks
                )
            }


            artworkDataSource
                .getArtworks(targetPage.toString())
                .onSuccess { response ->
                    val (newArtworks, pagination) = response
                    _state.update {
                        it.copy(
                            isLoading = false,
                            artworks = if (page == null) {
                                it.artworks + newArtworks.map { it.toArtworkUi() }
                            } else {
                                newArtworks.map { it.toArtworkUi() }
                            },
                            isEndReached = pagination.currentPage >= pagination.totalPages,
                            nextPage = pagination.currentPage + 1
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(ArtworkListEvent.Error(error))
                }
        }
    }
}