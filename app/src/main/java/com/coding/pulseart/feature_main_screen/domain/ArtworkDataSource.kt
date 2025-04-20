package com.coding.pulseart.feature_main_screen.domain

import com.coding.pulseart.core.domain.util.NetworkError
import com.coding.pulseart.core.domain.util.Result
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkFilterType
import com.coding.pulseart.feature_main_screen.presentation.models.Artwork
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkDetail
import com.coding.pulseart.feature_main_screen.presentation.models.Pagination
import com.coding.pulseart.feature_main_screen.presentation.models.SearchItem

interface ArtworkDataSource {
    suspend fun getArtworks(nextPage: String, filter: ArtworkFilterType): Result<Pair<List<Artwork>, Pagination>, NetworkError>
    suspend fun getArtwork(artworkId: String): Result<ArtworkDetail, NetworkError>
    suspend fun searchArtworks(query: String): Result<List<SearchItem>, NetworkError>
}