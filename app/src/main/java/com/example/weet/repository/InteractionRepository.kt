package com.example.weet.repository

import com.example.weet.data.local.dao.InteractionDao
import com.example.weet.data.local.model.InteractionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Interaction 도메인 모델 클래스
class Interaction(
    val id: Int = 0,
    val personId: Int,
    val date: Long,
    val emotionScore: Float?,
    val contacted: Boolean,
    val memo: String?
) {
    fun toEntity(): InteractionEntity {
        return InteractionEntity(
            id = id,
            personId = personId,
            date = date,
            emotionScore = emotionScore,
            contacted = contacted,
            memo = memo
        )
    }

    companion object {
        fun fromEntity(entity: InteractionEntity): Interaction {
            return Interaction(
                id = entity.id,
                personId = entity.personId,
                date = entity.date,
                emotionScore = entity.emotionScore,
                contacted = entity.contacted,
                memo = entity.memo
            )
        }
    }
}

// 인터페이스
interface InteractionRepository {
    fun getInteractionsByPersonId(personId: Int): Flow<List<Interaction>>
    suspend fun insertInteraction(interaction: Interaction)
    suspend fun deleteInteraction(interaction: Interaction)
}

// 구현체
class InteractionRepositoryImpl(
    private val dao: InteractionDao
) : InteractionRepository {

    override fun getInteractionsByPersonId(personId: Int): Flow<List<Interaction>> {
        return dao.getInteractionsByPersonId(personId).map { list ->
            list.map { Interaction.fromEntity(it) }
        }
    }

    override suspend fun insertInteraction(interaction: Interaction) {
        dao.insertInteraction(interaction.toEntity())
    }

    override suspend fun deleteInteraction(interaction: Interaction) {
        dao.deleteInteraction(interaction.toEntity())
    }
}
