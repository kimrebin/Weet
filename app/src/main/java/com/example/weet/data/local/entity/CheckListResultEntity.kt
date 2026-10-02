package com.example.weet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_result")
data class ChecklistResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val personId: Int, // 외래키: PersonEntity의 id

    // 각각의 질문에 대한 점수 (0.0 ~ 1.0 범위로 정규화된 값)
    val frequencyScore: Float,
    val emotionScore: Float,
    val distanceScore: Float,
    val tagWeight: Double,  // 예: 가족=1.2, 친구=1.0, 직장=0.8 등

    val rqsScore: Double,   // 계산된 최종 점수 (0.0 ~ 1.0)

    val timestamp: Long = System.currentTimeMillis() // 기록 시간
)
