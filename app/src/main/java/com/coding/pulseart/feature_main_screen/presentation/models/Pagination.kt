package com.coding.pulseart.feature_main_screen.presentation.models

data class Pagination(
    val totalItems: Int,
    val totalPages: Int,
    val currentPage: Int,
    val itemsPerPage: Int
)