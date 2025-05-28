package com.example.weet.repository

import com.example.weet.data.local.dao.ChecklistDao
import com.example.weet.data.local.entity.ChecklistResultEntity
import kotlinx.coroutines.flow.Flow

class ChecklistRepository(private val dao: ChecklistDao) {

    suspend fun insertChecklist(result: ChecklistResultEntity) {
        dao.insertChecklist(result)
    }

    fun getLatestChecklist(personId: Int): Flow<ChecklistResultEntity?> {
        return dao.getLatestChecklist(personId)
    }

    fun getChecklistHistory(personId: Int): Flow<List<ChecklistResultEntity>> {
        return dao.getChecklistHistory(personId)
    }
}
