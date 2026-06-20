package com.example.buurterij.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buurterij.data.ForagingSpotRepository
import com.example.buurterij.data.PlantCatalog
import com.example.buurterij.data.PlantCategory
import com.example.buurterij.data.PlantType
import com.example.buurterij.data.SpotPhotoEntity
import com.example.buurterij.data.toPlantType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SpotUiModel(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val plantType: PlantType,
    val lastVisitedAt: Long?,
    val notes: String?,
)

class ForagingViewModel(private val repository: ForagingSpotRepository) : ViewModel() {

    private val customTypes: StateFlow<List<PlantType>> = repository.getAllCustomTypes()
        .map { entities -> entities.map { it.toPlantType() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPlantTypes: StateFlow<Map<PlantCategory, List<PlantType>>> = customTypes
        .map { custom -> (PlantCatalog.all + custom).groupBy { it.category } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlantCatalog.groupedByCategory())

    val spots: StateFlow<List<SpotUiModel>> = combine(repository.getAllSpots(), customTypes) { entities, custom ->
        entities.mapNotNull { entity ->
            val plantType = PlantCatalog.byId(entity.plantTypeId) ?: custom.find { it.id == entity.plantTypeId }
            plantType?.let {
                SpotUiModel(
                    id = entity.id,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    plantType = it,
                    lastVisitedAt = entity.lastVisitedAt,
                    notes = entity.notes,
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSpot(lat: Double, lon: Double, plantTypeId: String) {
        viewModelScope.launch { repository.addSpot(lat, lon, plantTypeId) }
    }

    fun markVisited(spotId: Long) {
        viewModelScope.launch { repository.markVisited(spotId) }
    }

    fun deleteSpot(spotId: Long) {
        viewModelScope.launch { repository.deleteSpot(spotId) }
    }

    fun updateNotes(spotId: Long, notes: String) {
        viewModelScope.launch { repository.updateNotes(spotId, notes.ifBlank { null }) }
    }

    fun updatePlantType(spotId: Long, plantTypeId: String) {
        viewModelScope.launch { repository.updatePlantType(spotId, plantTypeId) }
    }

    fun addCustomType(
        dutchName: String,
        englishName: String,
        category: PlantCategory,
        seasonStartMonth: Int,
        seasonEndMonth: Int,
    ) {
        viewModelScope.launch {
            repository.addCustomType(dutchName, englishName, category, seasonStartMonth, seasonEndMonth)
        }
    }

    fun photosForSpot(spotId: Long): Flow<List<SpotPhotoEntity>> = repository.getPhotosForSpot(spotId)

    fun addPhoto(spotId: Long, filePath: String) {
        viewModelScope.launch { repository.addPhoto(spotId, filePath) }
    }
}
