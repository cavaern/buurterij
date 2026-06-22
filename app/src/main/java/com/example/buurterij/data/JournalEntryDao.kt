package com.example.buurterij.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {
    @Query("SELECT * FROM journal_entries ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<JournalEntryEntity>>

    @Insert
    suspend fun insert(entity: JournalEntryEntity): Long

    @Query("UPDATE journal_entries SET title = :title, content = :content, updatedAt = :updatedAt WHERE id = :id")
    suspend fun update(id: Long, title: String, content: String, updatedAt: Long)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun delete(id: Long)
}
