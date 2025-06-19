package com.example.weet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.repository.PersonRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: PersonRepository
) : ViewModel() {

    private val _relationshipScore = MutableStateFlow(100) // 기본값 또는 초기 점수
    val relationshipScore = _relationshipScore.asStateFlow()

    fun updateRelationshipScore(value: Int) {
        _relationshipScore.value = value
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _relationship = MutableStateFlow("")
    val relationship = _relationship.asStateFlow()

    private val _historyMessage = MutableStateFlow("")
    val historyMessage = _historyMessage.asStateFlow()

    private val _tagWeight = MutableStateFlow(1.0f)
    val tagWeight = _tagWeight.asStateFlow()

    fun updateTagWeight(value: Float) {
        _tagWeight.value = value
    }

    private val _photoUrl = MutableStateFlow("")
    val photoUrl = _photoUrl.asStateFlow()

    fun updatePhotoUrl(uri: String) {
        _photoUrl.value = uri
    }

    fun updateName(value: String) { _name.value = value }
    fun updateRelationship(value: String) { _relationship.value = value }
    fun updateHistoryMessage(value: String) { _historyMessage.value = value }

    fun savePerson(relationshipScore: Int = 50) {
        viewModelScope.launch {
            val person = PersonEntity(
                name = _name.value,
                relationship = _relationship.value,
                relationshipScore = relationshipScore,
                category = _historyMessage.value,
                id = 0,
                photoUrl = "",
                tag = "",
                score = 0
            )
            repository.insertPerson(person)
        }
    }
    fun loadPerson(personId: Int?) {
        viewModelScope.launch {
            if (personId != null) {
                repository.getPersonById(personId).collect { person ->
                    person?.let {
                        _relationshipScore.value = it.relationshipScore
                        _name.value = it.name
                        _relationship.value = it.relationship
                        _historyMessage.value = it.category
                    }
                }
            }
        }
    }
}
