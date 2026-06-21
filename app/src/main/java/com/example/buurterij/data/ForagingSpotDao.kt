package com.example.buurterij.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ForagingSpotDao {
    @Query("SELECT * FROM foraging_spots ORDER BY createdAt DESC")
    fun getAll(): Flow<List<ForagingSpotEntity>>

    @Insert
    suspend fun insert(spot: ForagingSpotEntity): Long

    @Query("DELETE FROM foraging_spots WHERE id = :spotId")
    suspend fun delete(spotId: Long)

    @Query("UPDATE foraging_spots SET lastVisitedAt = :timestamp WHERE id = :spotId")
    suspend fun updateLastVisited(spotId: Long, timestamp: Long)

    @Query("UPDATE foraging_spots SET notes = :notes WHERE id = :spotId")
    suspend fun updateNotes(spotId: Long, notes: String?)

    @Query("UPDATE foraging_spots SET plantTypeId = :plantTypeId WHERE id = :spotId")
    suspend fun updatePlantType(spotId: Long, plantTypeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM foraging_spots WHERE plantTypeId = :plantTypeId)")
    suspend fun existsWithPlantType(plantTypeId: String): Boolean
}
