package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailDto
import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail

fun ArtworkDetailDto.toArtworkDetail(): ArtworkDetail {
    return ArtworkDetail(
        id = id,
        title = title,
        artistDisplay = artist_display,
        imageUrl = "https://www.artic.edu/iiif/2/$image_id/full/400,/0/default.jpg"
    )
}