package com.example.weet.repository

import com.example.weet.data.local.dao.PersonDao
import com.example.weet.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

class PersonRepository(private val dao: PersonDao) {
    suspend fun insertPerson(person: PersonEntity) = dao.insert(person)
    fun getPersonById(id: Int): Flow<PersonEntity?> = dao.getPersonById(id)
}
