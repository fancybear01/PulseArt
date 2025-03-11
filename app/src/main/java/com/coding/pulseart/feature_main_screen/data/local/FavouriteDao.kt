package com.coding.pulseart.feature_main_screen.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavouriteEntity)

    @Delete
    suspend fun delete(favorite: FavouriteEntity)

    @Query("SELECT * FROM favourites")
    fun getAllFavorites(): Flow<List<FavouriteEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE id = :artworkId)")
    suspend fun isFavorite(artworkId: String): Boolean

}