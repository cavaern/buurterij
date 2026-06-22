package com.example.buurterij.ui

import com.example.buurterij.data.PlantCategory

data class MapFilterState(
    val showActive: Boolean = true,
    val showInactive: Boolean = true,
    val selectedCategories: Set<PlantCategory> = PlantCategory.entries.toSet(),
) {
    fun matches(spot: SpotUiModel, inSeason: Boolean): Boolean {
        if (inSeason && !showActive) return false
        if (!inSeason && !showInactive) return false
        if (spot.plantType.category !in selectedCategories) return false
        return true
    }
}
