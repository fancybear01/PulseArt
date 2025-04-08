package com.coding.pulseart.feature_main_screen.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val artistDisplay: String,
    val imageUrl: String,
    val dateStart: String = "0",
    val dateEnd: String = "0",
    val dateDisplay: String = "",
    val description: String? = "",
    val shortDescription: String? = ""
)