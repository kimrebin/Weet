package com.example.weet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val photoUrl: String?,
    val tag: String,
    val score: Int,
    val relationshipScore: Int,
    val relationship: String,
    val category: String,
)
