package com.coding.pulseart.feature_main_screen.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchDto(
    val id: Int,
    val api_link: String,
    val title: String
)