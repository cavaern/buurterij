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
}
