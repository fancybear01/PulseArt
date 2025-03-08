package com.coding.pulseart.feature_main_screen.domain

data class Artwork(
    val id: String,
    val title: String,
    val artistDisplay: String,
    val imageUrl: String,
    val nextPage: String? = null
)