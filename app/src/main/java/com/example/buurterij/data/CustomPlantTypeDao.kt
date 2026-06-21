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

    @Query(
        "UPDATE custom_plant_types SET dutchName = :dutchName, englishName = :englishName, " +
            "category = :category, seasonStartMonth = :seasonStartMonth, seasonEndMonth = :seasonEndMonth, " +
            "germanName = :germanName, frenchName = :frenchName, latinName = :latinName " +
            "WHERE id = :id",
    )
    suspend fun update(
        id: Long,
        dutchName: String,
        englishName: String,
        category: PlantCategory,
        seasonStartMonth: Int,
        seasonEndMonth: Int,
        germanName: String,
        frenchName: String,
        latinName: String,
    )

    @Query("DELETE FROM custom_plant_types WHERE id = :id")
    suspend fun delete(id: Long)
}
