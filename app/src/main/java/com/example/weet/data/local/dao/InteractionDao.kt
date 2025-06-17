package com.example.weet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weet.data.local.model.InteractionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionDao {
    @Query("SELECT * FROM interactions WHERE personId = :personId ORDER BY date DESC")
    fun getInteractionsByPersonId(personId: Int): Flow<List<InteractionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInteraction(interaction: InteractionEntity)

    @Delete
    suspend fun deleteInteraction(interaction: InteractionEntity)
}
