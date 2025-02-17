package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDto
import com.coding.pulseart.feature_main_screen.domain.Artwork
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

fun Artwork.toArtworkUi(): ArtworkUi {
    return ArtworkUi(
        id = id,
        title = title,
        artistDisplay = artistDisplay,
        imageUrl = imageUrl
    )
}