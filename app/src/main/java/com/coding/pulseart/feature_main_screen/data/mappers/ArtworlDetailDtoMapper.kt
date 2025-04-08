package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.local.FavouriteEntity
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailDto
import com.coding.pulseart.feature_main_screen.domain.ArtworkDetail
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkUi

fun ArtworkDetailDto.toArtworkDetail(): ArtworkDetail {
    return ArtworkDetail(
        id = id,
        title = title,
        artistDisplay = artist_display,
        imageUrl = "https://www.artic.edu/iiif/2/$image_id/full/400,/0/default.jpg",
        dateStart = date_start.toString(),
        dateEnd = date_end.toString(),
        dateDisplay = date_display,
        description = description ?: "",
        shortDescription = short_description ?: ""
    )
}

fun FavouriteEntity.toArtworkDetail(): ArtworkDetail {
    return ArtworkDetail(
        id = id,
        title = title,
        artistDisplay = artistDisplay,
        imageUrl = imageUrl,
        dateStart = dateStart,
        dateEnd = dateEnd,
        dateDisplay = dateDisplay,
        description = description ?: "",
        shortDescription = shortDescription ?: ""
    )
}

fun FavouriteEntity.toArtworkUi(): ArtworkUi {
    return ArtworkUi(
        id = id.toString(),
        title = title,
        artistDisplay = artistDisplay,
        imageUrl = imageUrl
    )
}

fun ArtworkDetail.toFavouriteEntity(): FavouriteEntity {
    return FavouriteEntity(
        id = id,
        title = title,
        artistDisplay = artistDisplay,
        imageUrl = imageUrl,
        dateStart = dateStart,
        dateEnd = dateEnd,
        dateDisplay = dateDisplay,
        description = description ?: "",
        shortDescription = shortDescription ?: ""
    )
}