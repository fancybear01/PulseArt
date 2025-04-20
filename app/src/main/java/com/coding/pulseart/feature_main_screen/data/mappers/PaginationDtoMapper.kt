package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.PaginationDto
import com.coding.pulseart.feature_main_screen.presentation.models.Pagination

fun PaginationDto.toPagination(): Pagination {
    return Pagination(
        totalItems = total,
        totalPages = total_pages,
        currentPage = current_page,
        itemsPerPage = limit
    )
}