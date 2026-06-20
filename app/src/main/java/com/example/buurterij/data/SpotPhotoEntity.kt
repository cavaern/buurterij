package com.example.buurterij.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "spot_photos", indices = [Index("spotId")])
data class SpotPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val spotId: Long,
    val filePath: String,
    val createdAt: Long = System.currentTimeMillis(),
)
