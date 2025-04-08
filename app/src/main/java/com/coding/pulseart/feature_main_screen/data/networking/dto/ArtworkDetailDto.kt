package com.coding.pulseart.feature_main_screen.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtworkDetailDto(
    val id: Int,
    val title: String,
    val artist_display: String,
    val image_id: String,
    val date_start: Int,
    val date_end: Int,
    val date_display: String,
    val description: String? = null,
    val short_description: String? = null
)
