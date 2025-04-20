package com.coding.pulseart.feature_main_screen.presentation.models

data class ArtworkDetail(
    val id: Int,
    val title: String,
    val artistDisplay: String,
    val imageUrl: String,
    val dateStart: String,
    val dateEnd: String,
    val dateDisplay: String,
    val description: String?,
    val shortDescription: String?
)