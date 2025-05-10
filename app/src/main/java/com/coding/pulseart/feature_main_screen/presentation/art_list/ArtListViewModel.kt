package com.coding.pulseart.feature_main_screen.presentation.art_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.pulseart.core.domain.util.onError
import com.coding.pulseart.core.domain.util.onSuccess
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
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

    private val initialPage = (1..30).random()

    private val _state = MutableStateFlow(ArtworkListState())
    val state = _state
        .onStart { onAction(ArtworkListAction.LoadInitial) }
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
                    loadArtworks(page = initialPage)
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
            is ArtworkListAction.FilterChanged -> {
                val newFilter = action.filter
                _state.update {
                    val cachedArtworks = it.cache[newFilter] ?: emptyList()

                    it.copy(
                        selectedFilter = newFilter,
                        artworks = cachedArtworks,
                        nextPage = if (cachedArtworks.isEmpty()) 1 else it.nextPage,
                        isEndReached = cachedArtworks.isNotEmpty() && it.isEndReached
                    )
                }

                if (state.value.artworks.isEmpty()) {
                    loadArtworks(page = 1)
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
                    artworks = if (page == initialPage) emptyList() else it.artworks
                )
            }


            artworkDataSource
                .getArtworks(targetPage.toString(), state.value.selectedFilter)
                .onSuccess { response ->
                    val (newArtworks, pagination) = response
                    _state.update {

                        val updatedCache = it.cache.toMutableMap().apply {
                            val existing = get(state.value.selectedFilter) ?: emptyList()
                            put(state.value.selectedFilter, existing + newArtworks)
                        }

                        it.copy(
                            isLoading = false,
                            artworks = it.artworks + newArtworks,
                            cache = updatedCache,
                            isEndReached = pagination.currentPage >= pagination.totalPages,
                            nextPage = if (page == initialPage) initialPage + 1 else pagination.currentPage + 1
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