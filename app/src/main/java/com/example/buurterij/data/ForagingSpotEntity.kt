package com.example.buurterij.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foraging_spots")
data class ForagingSpotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val plantTypeId: String,
    val lastVisitedAt: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)
