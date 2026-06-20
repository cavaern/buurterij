package com.example.buurterij.data

import androidx.room.TypeConverter

class PlantCategoryConverter {
    @TypeConverter
    fun fromCategory(category: PlantCategory): String = category.name

    @TypeConverter
    fun toCategory(value: String): PlantCategory = PlantCategory.valueOf(value)
}
