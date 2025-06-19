// PersonRepositoryImpl.kt
package com.example.weet.repository

import com.example.weet.data.local.dao.PersonDao
import com.example.weet.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val dao: PersonDao
) : PersonRepository {

    override suspend fun insertPerson(person: PersonEntity) {
        dao.insertPerson(person)
    }

    override fun getAllPersons(): Flow<List<PersonEntity>> {
        return dao.getAllPersons()
    }

    override suspend fun getPersonById(id: Int): PersonEntity? {
        return dao.getPersonById(id)
    }
}