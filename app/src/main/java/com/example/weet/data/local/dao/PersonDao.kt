package com.example.weet.data.local.dao

import androidx.room.*
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
    fun getAllPersons(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE id = :id")
    fun getPersonById(id: Int): Flow<PersonEntity?>

    @Query("SELECT * FROM persons WHERE id = :id")
    suspend fun getPersonByIdOnce(id: Int): PersonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Delete
    suspend fun deletePerson(person: PersonEntity)
}

