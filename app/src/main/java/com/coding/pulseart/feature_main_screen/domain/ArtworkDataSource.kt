package com.coding.pulseart.feature_main_screen.domain

import com.coding.pulseart.core.domain.util.NetworkError
import com.coding.pulseart.core.domain.util.Result

interface ArtworkDataSource {
    suspend fun getArtworks(): Result<List<Artwork>, NetworkError>
}