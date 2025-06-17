package com.example.weet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weet.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM persons")
    fun getAllPersons(): Flow<List<com.example.weet.data.local.entity.PersonEntity>>

    @Query("SELECT * FROM persons WHERE id = :id")
    fun getPersonById(id: Int): Flow<com.example.weet.data.local.entity.PersonEntity?>

    @Query("SELECT * FROM persons WHERE id = :id")
    suspend fun getPersonByIdOnce(id: Int): com.example.weet.data.local.entity.PersonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: com.example.weet.data.local.entity.PersonEntity)

    @Delete
    suspend fun deletePerson(person: com.example.weet.data.local.entity.PersonEntity)
}

