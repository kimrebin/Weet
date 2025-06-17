package com.example.weet.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "interactions",
    foreignKeys = [
        ForeignKey(
            entity = com.example.weet.data.local.entity.PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InteractionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val personId: Int, // 누구와의 상호작용인지

    val date: Long,  // 타임스탬프 (예: System.currentTimeMillis())
    val emotionScore: Float?, // 점수 (nullable)
    val contacted: Boolean,   // 연락 여부
    val memo: String?         // 자유 메모 (optional)
)
