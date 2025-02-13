package com.coding.pulseart.feature_main_screen.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtworkResponseDto(
    val data: List<ArtworkDto>,
    val pagination: PaginationDto
)
