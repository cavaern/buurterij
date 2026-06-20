package com.example.buurterij.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomPlantTypeDao {
    @Query("SELECT * FROM custom_plant_types ORDER BY dutchName ASC")
    fun getAll(): Flow<List<CustomPlantTypeEntity>>

    @Insert
    suspend fun insert(entity: CustomPlantTypeEntity): Long
}
