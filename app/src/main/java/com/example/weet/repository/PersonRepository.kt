package com.example.weet.repository

import com.example.weet.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun insertPerson(person: PersonEntity)
    fun getAllPersons(): Flow<List<PersonEntity>>
    suspend fun getPersonById(id: Int): PersonEntity?
}
