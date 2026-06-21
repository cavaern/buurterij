package com.example.buurterij.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ForagingSpotEntity::class, CustomPlantTypeEntity::class, SpotPhotoEntity::class],
    version = 3,
    exportSchema = false,
)
@TypeConverters(PlantCategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foragingSpotDao(): ForagingSpotDao
    abstract fun customPlantTypeDao(): CustomPlantTypeDao
    abstract fun spotPhotoDao(): SpotPhotoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "buurterij.db",
                ).fallbackToDestructiveMigration(dropAllTables = true).build().also { INSTANCE = it }
            }
    }
}
