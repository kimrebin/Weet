package com.example.weet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weet.data.local.dao.ChecklistDao
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: PersonRepository,
    private val checklistDao: ChecklistDao // ⭐ 체크리스트 DAO 주입
) : ViewModel() {

    private val _relationshipScore = MutableStateFlow(100) // 기본값
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

    fun updatePhotoUrl(url: String) {
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

        // 1. 기본 프로필 정보 로드
        viewModelScope.launch {
            repository.getPersonById(personId).collect { person ->
                _name.value = person.name
                _relationship.value = person.tag
                _photoUrl.value = person.photoUrl ?: ""
                _historyMessage.value = person.historyMessage ?: ""
                _relationshipScore.value = person.relationshipScore
            }
        }

        // 2. 체크리스트 최신 점수 반영
        viewModelScope.launch {
            checklistDao.getLatestChecklist(personId).collect { checklist ->
                checklist?.let {
                    val score = (it.rqsScore * 100).toInt()
                    _relationshipScore.value = score
                }
            }
        }
    }
}
