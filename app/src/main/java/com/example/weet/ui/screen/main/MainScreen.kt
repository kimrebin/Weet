package com.example.weet.ui.screen.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.relationshiptracker.viewmodel.MainViewModel
import com.example.relationshiptracker.domain.model.Person

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val persons = viewModel.persons.collectAsState().value
    LazyColumn {
        items(persons) { person ->
            Text(text = person.name)
        }
    }
}
