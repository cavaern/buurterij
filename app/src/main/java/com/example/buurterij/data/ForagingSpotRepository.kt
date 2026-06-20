package com.example.buurterij.data

import kotlinx.coroutines.flow.Flow

class ForagingSpotRepository(private val dao: ForagingSpotDao) {
    fun getAllSpots(): Flow<List<ForagingSpotEntity>> = dao.getAll()

    suspend fun addSpot(lat: Double, lon: Double, plantTypeId: String): Long =
        dao.insert(ForagingSpotEntity(latitude = lat, longitude = lon, plantTypeId = plantTypeId))

    suspend fun deleteSpot(id: Long) = dao.delete(id)

    suspend fun markVisited(id: Long, timestamp: Long = System.currentTimeMillis()) =
        dao.updateLastVisited(id, timestamp)
}
