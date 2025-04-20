package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.local.FavouriteEntity
import com.coding.pulseart.feature_main_screen.data.networking.dto.ArtworkDetailDto
import com.coding.pulseart.feature_main_screen.presentation.models.ArtworkDetail
import com.coding.pulseart.feature_main_screen.presentation.models.Artwork

fun ArtworkDetailDto.toArtworkDetail(): ArtworkDetail {
    return ArtworkDetail(
        id = id,
        title = title,
        artistDisplay = artist_display,
        imageUrl = "https://www.artic.edu/iiif/2/$image_id/full/400,/0/default.jpg",
        dateStart = date_start.toString(),
        dateEnd = date_end.toString(),
        dateDisplay = date_display,
        description = description?.deleteTags() ?: "",
        shortDescription = short_description?.deleteTags() ?: ""
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

fun FavouriteEntity.toArtwork(): Artwork {
    return Artwork(
        id = id.toString(),
        title = title,
        artistDisplay = artistDisplay,
        imageUrl = imageUrl,
        nextPage = "",
        artworkType = ""
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

fun String.deleteTags(): String {
    return this.replace(Regex("(?s)<.*?>"), "")
}