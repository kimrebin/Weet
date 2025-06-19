package com.example.weet.ui.screen.relationshipMap

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weet.viewmodel.MainViewModel
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun RelationshipScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val friends by viewModel.allFriends.collectAsState(initial = emptyList())

    RelationshipMap(friends = friends)

    IconButton(onClick = onBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}
