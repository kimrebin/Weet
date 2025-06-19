package com.example.weet.viewmodel

import androidx.compose.ui.text.LinkAnnotation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
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

    fun updatePhotoUrl(url : String) {
        _photoUrl.value = url
    }

    private var currentPersonId: Int? = null

    fun updateName(value: String) { _name.value = value }
    fun updateRelationship(value: String) { _relationship.value = value }
    fun updateHistoryMessage(value: String) { _historyMessage.value = value }

    fun savePerson(relationshipScore: Int = 100) {
        viewModelScope.launch {
            val id = currentPersonId ?: return@launch
            val person = PersonEntity(
                id = id,
                name = _name.value,
                tag = _relationship.value,
                photoUrl = _photoUrl.value,
                score = 0,
                relationshipScore = relationshipScore,
                relationship = "",
                category = "",
                historyMessage = _historyMessage.value
            )
            repository.insertPerson(person)
        }
    }
    fun loadPerson(personId: Int) {
        currentPersonId = personId
        viewModelScope.launch {
            val person = repository.getPersonById(personId)
            person?.let {
                _name.value = it.name
                _relationship.value = it.tag
                _photoUrl.value = it.photoUrl ?: ""
                _historyMessage.value = it.historyMessage ?: ""
                _relationshipScore.value = it.relationshipScore
            }
        }
    }
}
