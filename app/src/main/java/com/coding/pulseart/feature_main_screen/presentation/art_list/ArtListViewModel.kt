package com.coding.pulseart.feature_main_screen.presentation.art_list

import androidx.lifecycle.ViewModel
import com.coding.pulseart.feature_main_screen.domain.ArtworkDataSource
import kotlinx.coroutines.flow.MutableStateFlow

class ArtListViewModel(
    private val artworkDataSource: ArtworkDataSource
) : ViewModel() {

}