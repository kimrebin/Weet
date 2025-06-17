package com.example.weet.ui.screen.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weet.viewmodel.MainViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val personList = viewModel.persons.collectAsState().value
    LazyColumn {
        items(personList) { personId ->
            Text(
                text = "Person: $personId",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {onPersonClick(personId)}
                    .padding(16.dp)
            )
        }
    }
}
