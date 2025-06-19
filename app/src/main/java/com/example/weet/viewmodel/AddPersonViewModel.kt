package com.example.weet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPersonViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    fun addPerson(name: String, tag: String) {
        val person = PersonEntity(
            id = 0,
            name = name,
            tag = tag,
            photoUrl = "",
            score = 0,
            relationshipScore = 100,
            relationship = "",
            category = ""
        )
        viewModelScope.launch {
            repository.insertPerson(person)
        }
    }
}