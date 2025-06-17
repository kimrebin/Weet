package com.example.weet.repository

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weet.data.local.dao.PersonDao
import com.example.weet.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

/*
class PersonRepository(private val dao: PersonDao) {
    suspend fun insertPerson(person: PersonEntity) = dao.insertPerson(person)
    fun getPersonById(id: Int): Flow<PersonEntity?> {
        return dao.getPersonById(id)
    }
}
*/

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val photoUrl: String?,
    val tag: String,
    val score: Int
)

data class Person(
    val id: Int,
    val name: String,
    val photoUrl: String?,
    val tag: String,
    val score: Int
)

fun PersonEntity.toDomainModel(): Person {
    return Person(id, name, photoUrl, tag, score)
}

fun Person.toEntity(): PersonEntity {
    return PersonEntity(id, name, photoUrl, tag, score)
}


