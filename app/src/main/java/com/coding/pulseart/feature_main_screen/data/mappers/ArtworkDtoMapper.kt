package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDto
import com.coding.pulseart.feature_main_screen.domain.Artwork

fun ArtworkDto.toArtwork(): Artwork {
    return Artwork(
        id = id,
        title = title,
        artistDisplay = artist_display,
        imageId = image_id
    )
}