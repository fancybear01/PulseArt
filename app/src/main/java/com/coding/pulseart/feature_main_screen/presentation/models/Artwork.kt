package com.coding.pulseart.feature_main_screen.presentation.models

data class Artwork(
    val id: String,
    val title: String,
    val artistDisplay: String,
    val imageUrl: String,
    val nextPage: String? = null,
    val artworkType: String
)