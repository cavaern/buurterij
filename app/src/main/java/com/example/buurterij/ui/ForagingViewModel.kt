package com.example.buurterij.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buurterij.data.ForagingSpotRepository
import com.example.buurterij.data.PlantCatalog
import com.example.buurterij.data.PlantType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    val spots: StateFlow<List<SpotUiModel>> = repository.getAllSpots()
        .map { entities ->
            entities.mapNotNull { entity ->
                PlantCatalog.byId(entity.plantTypeId)?.let { plantType ->
                    SpotUiModel(
                        id = entity.id,
                        latitude = entity.latitude,
                        longitude = entity.longitude,
                        plantType = plantType,
                        lastVisitedAt = entity.lastVisitedAt,
                        notes = entity.notes,
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSpot(lat: Double, lon: Double, plantTypeId: String) {
        viewModelScope.launch { repository.addSpot(lat, lon, plantTypeId) }
    }

    fun markVisited(spotId: Long) {
        viewModelScope.launch { repository.markVisited(spotId) }
    }

    fun deleteSpot(spotId: Long) {
        viewModelScope.launch { repository.deleteSpot(spotId) }
    }
}
