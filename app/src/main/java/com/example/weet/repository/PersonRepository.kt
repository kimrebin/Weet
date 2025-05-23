package com.example.weet.repository

import com.example.relationshiptracker.data.local.dao.PersonDao
import com.example.relationshiptracker.domain.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val dao: PersonDao
) {
    fun getAllPersons(): Flow<List<Person>> =
        dao.getAllPersons().map { list ->
            list.map {
                Person(
                    id = it.id,
                    name = it.name,
                    photoUrl = it.photoUrl,
                    tag = it.tag,
                    score = it.score
                )
            }
        }
}