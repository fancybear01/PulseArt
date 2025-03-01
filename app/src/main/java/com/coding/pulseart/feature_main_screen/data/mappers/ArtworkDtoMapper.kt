package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDto
import com.coding.pulseart.feature_main_screen.domain.Artwork

fun ArtworkDto.toArtwork(): Artwork {
    return Artwork(
        id = id.toString(),
        title = title,
        artistDisplay = artist_display,
        imageUrl = "https://www.artic.edu/iiif/2/$image_id/full/400,/0/default.jpg"
    )
}