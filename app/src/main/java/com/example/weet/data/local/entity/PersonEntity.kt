package com.example.weet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.wire.Message

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val photoUrl: String?,
    val tag: String,
    val score: Int,
    val historyMessage: String?,
    val relationshipScore: Int,
    val relationship: String,
    val category: String,
)
// relationshipMap UI를 위해
data class Friend(
    val name: String,
    val score: Int
)

fun PersonEntity.toFriend(): Friend {
    return Friend(
        name = this.name,
        score = this.relationshipScore
    )
}
