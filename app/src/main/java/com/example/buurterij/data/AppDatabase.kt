package com.example.buurterij.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Schema bumps from here on must ship a real Migration (see Migrations.kt) and be added to
// MIGRATIONS below. Without one, Room throws on launch instead of silently wiping user data —
// that's intentional: fallbackToDestructiveMigrationOnDowngrade only covers version downgrades,
// not the normal case of shipping a new schema forward.
@Database(
    entities = [ForagingSpotEntity::class, CustomPlantTypeEntity::class, SpotPhotoEntity::class, JournalEntryEntity::class],
    version = 4,
    exportSchema = true,
)
@TypeConverters(PlantCategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foragingSpotDao(): ForagingSpotDao
    abstract fun customPlantTypeDao(): CustomPlantTypeDao
    abstract fun spotPhotoDao(): SpotPhotoDao
    abstract fun journalEntryDao(): JournalEntryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "buurterij.db",
                )
                    .addMigrations(*MIGRATIONS)
                    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
