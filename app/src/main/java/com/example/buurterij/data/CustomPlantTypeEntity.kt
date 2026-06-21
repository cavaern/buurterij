package com.example.buurterij.data

import androidx.room.Entity
import androidx.room.PrimaryKey

private const val CUSTOM_PLANT_TYPE_ID_PREFIX = "custom-"

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

/** Builds the [PlantType.id] used for the custom type with the given database [id]. */
fun customPlantTypeId(id: Long): String = "$CUSTOM_PLANT_TYPE_ID_PREFIX$id"

/** Whether this [PlantType.id] refers to a DB-backed custom type rather than a built-in one. */
fun String.isCustomPlantTypeId(): Boolean = startsWith(CUSTOM_PLANT_TYPE_ID_PREFIX)

/** Extracts the [CustomPlantTypeEntity.id] from a [PlantType.id], or null if it's not a custom type id. */
fun String.toCustomPlantTypeDbId(): Long? =
    removePrefix(CUSTOM_PLANT_TYPE_ID_PREFIX).takeIf { it != this }?.toLongOrNull()

fun CustomPlantTypeEntity.toPlantType(): PlantType = PlantType(
    id = customPlantTypeId(id),
    dutchName = dutchName,
    englishName = englishName,
    category = category,
    seasonStartMonth = seasonStartMonth,
    seasonEndMonth = seasonEndMonth,
    germanName = germanName,
    frenchName = frenchName,
    latinName = latinName,
)
