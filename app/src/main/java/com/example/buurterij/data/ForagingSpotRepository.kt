package com.example.buurterij.data

import java.io.File
import kotlinx.coroutines.flow.Flow

class ForagingSpotRepository(
    private val dao: ForagingSpotDao,
    private val customPlantTypeDao: CustomPlantTypeDao,
    private val spotPhotoDao: SpotPhotoDao,
    private val journalEntryDao: JournalEntryDao,
) {
    fun getAllSpots(): Flow<List<ForagingSpotEntity>> = dao.getAll()

    suspend fun addSpot(lat: Double, lon: Double, plantTypeId: String): Long =
        dao.insert(ForagingSpotEntity(latitude = lat, longitude = lon, plantTypeId = plantTypeId))

    suspend fun deleteSpot(id: Long) {
        spotPhotoDao.getForSpotOnce(id).forEach { photo -> File(photo.filePath).delete() }
        spotPhotoDao.deleteForSpot(id)
        dao.delete(id)
    }

    suspend fun markVisited(id: Long, timestamp: Long = System.currentTimeMillis()) =
        dao.updateLastVisited(id, timestamp)

    suspend fun updateNotes(id: Long, notes: String?) = dao.updateNotes(id, notes)

    suspend fun updatePlantType(id: Long, plantTypeId: String) = dao.updatePlantType(id, plantTypeId)

    fun getAllCustomTypes(): Flow<List<CustomPlantTypeEntity>> = customPlantTypeDao.getAll()

    suspend fun addCustomType(
        dutchName: String,
        englishName: String,
        category: PlantCategory,
        seasonStartMonth: Int,
        seasonEndMonth: Int,
        germanName: String = "",
        frenchName: String = "",
        latinName: String = "",
    ): Long = customPlantTypeDao.insert(
        CustomPlantTypeEntity(
            dutchName = dutchName,
            englishName = englishName,
            category = category,
            seasonStartMonth = seasonStartMonth,
            seasonEndMonth = seasonEndMonth,
            germanName = germanName,
            frenchName = frenchName,
            latinName = latinName,
        ),
    )

    suspend fun updateCustomType(
        id: Long,
        dutchName: String,
        englishName: String,
        category: PlantCategory,
        seasonStartMonth: Int,
        seasonEndMonth: Int,
        germanName: String = "",
        frenchName: String = "",
        latinName: String = "",
    ) = customPlantTypeDao.update(
        id, dutchName, englishName, category, seasonStartMonth, seasonEndMonth, germanName, frenchName, latinName,
    )

    /**
     * Deletes the custom type with the given [id], unless at least one spot still references it.
     * Returns `true` if the type was deleted, `false` if deletion was blocked because it's in use.
     */
    suspend fun deleteCustomType(id: Long): Boolean {
        val inUse = dao.existsWithPlantType(customPlantTypeId(id))
        if (inUse) return false
        customPlantTypeDao.delete(id)
        return true
    }

    fun getPhotosForSpot(spotId: Long): Flow<List<SpotPhotoEntity>> = spotPhotoDao.getForSpot(spotId)

    suspend fun addPhoto(spotId: Long, filePath: String): Long =
        spotPhotoDao.insert(SpotPhotoEntity(spotId = spotId, filePath = filePath))

    fun getAllJournalEntries(): Flow<List<JournalEntryEntity>> = journalEntryDao.getAll()

    suspend fun addJournalEntry(title: String, content: String): Long =
        journalEntryDao.insert(JournalEntryEntity(title = title, content = content))

    suspend fun updateJournalEntry(id: Long, title: String, content: String) =
        journalEntryDao.update(id, title, content, System.currentTimeMillis())

    suspend fun deleteJournalEntry(id: Long) = journalEntryDao.delete(id)
}
