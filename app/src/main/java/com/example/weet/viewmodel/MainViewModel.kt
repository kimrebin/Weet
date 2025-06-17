package com.example.weet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.weet.repository.PersonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.StructureKind
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    val personByTag = repository.getAllPersons()
        .map { list: List<com.example.weet.repository.Person> ->
            list.groupBy { it.tag }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}



