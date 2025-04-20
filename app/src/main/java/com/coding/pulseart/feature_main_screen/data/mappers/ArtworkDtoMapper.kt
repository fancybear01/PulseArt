package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDto
import com.coding.pulseart.feature_main_screen.presentation.models.Artwork

fun ArtworkDto.toArtwork(): Artwork {
    return Artwork(
        id = id.toString(),
        title = title,
        artistDisplay = artist_display,
        imageUrl = image_id?.let {
            "https://www.artic.edu/iiif/2/$it/full/400,/0/default.jpg"
        } ?: "https://static.vecteezy.com/system/resources/thumbnails/022/059/000/small_2x/no-image-available-icon-vector.jpg",
        artworkType = artwork_type_title
    )
}