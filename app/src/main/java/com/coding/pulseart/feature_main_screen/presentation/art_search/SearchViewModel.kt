package com.coding.pulseart.feature_main_screen.presentation.art_search

import androidx.lifecycle.ViewModel
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import com.coding.pulseart.core.domain.util.onError
import com.coding.pulseart.core.domain.util.onSuccess

class SearchViewModel(
    private val artworkDataSource: ArtworkDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state = _state

    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.SearchArtworks -> searchArtworks()
        }
    }

    fun clearSearch() {
        _state.update {
            it.copy(
                searchQuery = "",
                results = emptyList(),
                error = null
            )
        }
    }

    fun searchArtworks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = false, error = null) }

            artworkDataSource
                .searchArtworks(_state.value.searchQuery)
                .onSuccess { results ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            results = results
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(SearchEvent.Error(error))
                }
        }
    }
}

