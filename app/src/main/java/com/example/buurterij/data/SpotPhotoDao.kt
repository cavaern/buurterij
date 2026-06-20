package com.example.buurterij.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotPhotoDao {
    @Query("SELECT * FROM spot_photos WHERE spotId = :spotId ORDER BY createdAt DESC")
    fun getForSpot(spotId: Long): Flow<List<SpotPhotoEntity>>

    @Query("SELECT * FROM spot_photos WHERE spotId = :spotId")
    suspend fun getForSpotOnce(spotId: Long): List<SpotPhotoEntity>

    @Insert
    suspend fun insert(entity: SpotPhotoEntity): Long

    @Query("DELETE FROM spot_photos WHERE spotId = :spotId")
    suspend fun deleteForSpot(spotId: Long)
}
