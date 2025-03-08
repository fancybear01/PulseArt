package com.coding.pulseart.feature_main_screen.domain

data class Pagination(
    val totalItems: Int,
    val totalPages: Int,
    val currentPage: Int,
    val itemsPerPage: Int
)