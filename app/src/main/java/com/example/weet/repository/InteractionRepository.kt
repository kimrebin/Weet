package com.example.weet.repository

import com.example.weet.data.local.dao.InteractionDao
import com.example.weet.data.local.model.InteractionEntity


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 도메인 모델
data class Interaction(
    val id: Int = 0,
    val personId: Int,
    val date: Long,
    val emotionScore: Float?,
    val contacted: Boolean,
    val memo: String?
)

// 매핑 확장 함수
fun InteractionEntity.toDomainModel(): Interaction {
    return Interaction(
        id = id,
        personId = personId,
        date = date,
        emotionScore = emotionScore,
        contacted = contacted,
        memo = memo
    )
}

fun Interaction.toEntity(): InteractionEntity {
    return InteractionEntity(
        id = id,
        personId = personId,
        date = date,
        emotionScore = emotionScore,
        contacted = contacted,
        memo = memo
    )
}

interface InteractionRepository {
    fun getInteractionsByPersonId(personId: Int): Flow<List<Interaction>>
    suspend fun insertInteraction(interaction: Interaction)
    suspend fun deleteInteraction(interaction: Interaction)
}


class InteractionRepositoryImpl(
    private val dao: InteractionDao
) : InteractionRepository {

    override fun getInteractionsByPersonId(personId: Int): Flow<List<Interaction>> {
        return dao.getInteractionsByPersonId(personId).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun insertInteraction(interaction: Interaction) {
        dao.insertInteraction(interaction.toEntity())
    }

    override suspend fun deleteInteraction(interaction: Interaction) {
        dao.deleteInteraction(interaction.toEntity())
    }
}
