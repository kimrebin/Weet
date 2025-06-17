package com.example.weet.data.local.dao

import androidx.room.*
import com.example.weet.data.local.entity.ChecklistResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklist(result: ChecklistResultEntity)

    @Query("SELECT * FROM checklist_result WHERE personId = :personId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestChecklist(personId: Int): Flow<ChecklistResultEntity?>

    @Query("SELECT * FROM checklist_result WHERE personId = :personId ORDER BY timestamp DESC")
    fun getChecklistHistory(personId: Int): Flow<List<ChecklistResultEntity>>
}

