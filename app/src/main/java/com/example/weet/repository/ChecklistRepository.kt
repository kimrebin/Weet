package com.example.weet.repository

import com.example.weet.data.local.dao.ChecklistDao
import com.example.weet.data.local.dao.PersonDao
import com.example.weet.data.local.entity.ChecklistResultEntity
import kotlinx.coroutines.flow.Flow
import kotlin.math.roundToInt

class ChecklistRepository(
    private val checklistDao: ChecklistDao,
    private val personDao: PersonDao
) {

    suspend fun insertChecklistAndUpdateScore(result: ChecklistResultEntity, tagWeight: Double) {
        checklistDao.insertChecklist(result)

        // 점수 계산
        val score = calculateRQS(
            result.frequencyScore,
            result.emotionScore,
            result.distanceScore,
            tagWeight
        ).roundToInt()

        // person 불러와서 score 업데이트
        val person = personDao.getPersonByIdOnce(result.personId)
        person?.let {
            val updated = it.copy(relationshipScore = score)
            personDao.insertPerson(updated)
        }
    }

    suspend fun updatePersonScore(personId: Int, score: Int) {
        val person = personDao.getPersonByIdOnce(personId)
        person?.let {
            val updated = it.copy(relationshipScore = score)
            personDao.insertPerson(updated)
        }
    }

    fun getLatestChecklist(personId: Int): Flow<ChecklistResultEntity?> {
        return checklistDao.getLatestChecklist(personId)
    }

    fun getChecklistHistory(personId: Int): Flow<List<ChecklistResultEntity>> {
        return checklistDao.getChecklistHistory(personId)
    }

    private fun calculateRQS(frequency: Float, emotion: Float, distance: Float, tagWeight: Double): Double {
        val w1 = 0.4f
        val w2 = 0.3f
        val w3 = 0.2f
        val w4 = 0.1f
        return w1 * frequency + w2 * emotion + w3 * distance + w4 * tagWeight
    }
}

