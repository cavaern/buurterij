package com.example.buurterij.ui

import com.example.buurterij.data.Language
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.displayName

data class MapFilterState(
    val showActive: Boolean = true,
    val showInactive: Boolean = true,
    val selectedCategories: Set<PlantCategory> = PlantCategory.entries.toSet(),
    val searchQuery: String = "",
) {
    fun matches(
        spot: SpotUiModel,
        inSeason: Boolean,
        mainLanguage: Language,
        secondaryLanguage: Language?,
    ): Boolean {
        if (inSeason && !showActive) return false
        if (!inSeason && !showInactive) return false
        if (spot.plantType.category !in selectedCategories) return false
        if (searchQuery.isNotBlank()) {
            val name = spot.plantType.displayName(mainLanguage, secondaryLanguage)
            if (!name.contains(searchQuery, ignoreCase = true)) return false
        }
        return true
    }
}
