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

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _relationship = MutableStateFlow("")
    val relationship = _relationship.asStateFlow()

    private val _historyMessage = MutableStateFlow("")
    val historyMessage = _historyMessage.asStateFlow()

    fun updateName(value: String) { _name.value = value }
    fun updateRelationship(value: String) { _relationship.value = value }
    fun updateHistoryMessage(value: String) { _historyMessage.value = value }

    fun savePerson(relationshipScore: Int = 87) {
        viewModelScope.launch {
            val person = PersonEntity(
                name = _name.value,
                relationship = _relationship.value,
                relationshipScore = relationshipScore,
                category = _historyMessage.value
            )
            repository.insertPerson(person)
        }
    }
}
