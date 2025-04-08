package com.coding.pulseart.feature_main_screen.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FavouriteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract val dao: FavouriteDao

    companion object {
        @Volatile
        private var Instance: FavouriteDatabase? = null

        fun getDatabase(context: Context): FavouriteDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FavouriteDatabase::class.java,
                    "favourites_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE favourites ADD COLUMN dateStart INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "ALTER TABLE favourites ADD COLUMN dateEnd INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "ALTER TABLE favourites ADD COLUMN dateDisplay TEXT NOT NULL DEFAULT ''"
        )
        database.execSQL(
            "ALTER TABLE favourites ADD COLUMN description TEXT NOT NULL DEFAULT ''"
        )
        database.execSQL(
            "ALTER TABLE favourites ADD COLUMN shortDescription TEXT NOT NULL DEFAULT ''"
        )
    }
}