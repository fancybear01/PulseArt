package com.coding.pulseart.feature_main_screen.data.mappers

import com.coding.pulseart.feature_main_screen.data.networking.dto.SearchDto
import com.coding.pulseart.feature_main_screen.domain.SearchItem

fun SearchDto.toSearchItem() : SearchItem {
    return SearchItem(
        id = id.toString(),
        apiLink = api_link,
        title = title
    )
}