package com.example.weet.repository

import com.example.weet.data.local.dao.PersonDao
import com.example.weet.data.local.entity.PersonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// 도메인/화면에서 사용하는 모델
data class Person(
    val id: Int,
    val name: String,
    val photoUrl: String?,
    val tag: String,
    val score: Int,
    val relationshipScore: Int,
    val relationship: String,
    val category: String,
)

// Entity ↔ Domain 변환 함수
fun PersonEntity.toDomain(): Person =
    Person(id, name, photoUrl, tag, score, relationshipScore, relationship, category)

fun Person.toEntity(): PersonEntity =
    PersonEntity(id, name, photoUrl, tag, score, relationshipScore, relationship, category)

// Repository 클래스
class PersonRepository @Inject constructor(private val dao: PersonDao) {

    suspend fun insertPerson(person: PersonEntity) {
        dao.insertPerson(person)
    }

    fun getPersonById(id: Int?): Flow<Person?> {
        return dao.getPersonById(id).map { it?.toDomain() }
    }

    fun getAllPersons(): Flow<List<Person>> {
        return dao.getAllPersons().map { list -> list.map { it.toDomain() } }
    }
}
