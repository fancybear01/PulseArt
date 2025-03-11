package com.coding.pulseart.feature_main_screen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavouriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract val dao: FavouriteDao
}