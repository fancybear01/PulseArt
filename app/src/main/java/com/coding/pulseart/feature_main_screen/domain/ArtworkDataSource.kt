package com.coding.pulseart.feature_main_screen.domain

import com.coding.pulseart.core.domain.util.Error
import com.coding.pulseart.core.domain.util.NetworkError
import com.coding.pulseart.core.domain.util.Result

interface ArtworkDataSource {
    suspend fun getArtworks(nextPage: String): Result<Pair<List<Artwork>, Pagination>, NetworkError>
    suspend fun getArtwork(artworkId: String): Result<ArtworkDetail, NetworkError>
    suspend fun searchArtworks(query: String): Result<List<Artwork>, NetworkError>
}