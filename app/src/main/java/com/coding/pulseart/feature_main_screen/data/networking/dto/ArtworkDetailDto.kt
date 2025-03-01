package com.coding.pulseart.feature_main_screen.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtworkDetailDto(
    val id: Int,
    val title: String,
    val artist_display: String,
    val image_id: String
)
