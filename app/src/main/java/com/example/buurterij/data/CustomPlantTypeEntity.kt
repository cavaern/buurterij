package com.example.buurterij.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_plant_types")
data class CustomPlantTypeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dutchName: String,
    val englishName: String,
    val category: PlantCategory,
    val seasonStartMonth: Int,
    val seasonEndMonth: Int,
    val germanName: String = "",
    val frenchName: String = "",
    val latinName: String = "",
)

fun CustomPlantTypeEntity.toPlantType(): PlantType = PlantType(
    id = "custom-$id",
    dutchName = dutchName,
    englishName = englishName,
    category = category,
    seasonStartMonth = seasonStartMonth,
    seasonEndMonth = seasonEndMonth,
    germanName = germanName,
    frenchName = frenchName,
    latinName = latinName,
)
