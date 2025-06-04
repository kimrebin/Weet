package com.example.weet.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weet.ui.components.ScoreDonut
import com.example.weet.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    personId: Int,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadPerson(personId)
    }

    val score by viewModel.relationshipScore.collectAsState()

    Column {
        Text("Profile Page")
        Spacer(modifier = Modifier.height(16.dp))
        ScoreDonut(score = score)
    }
}
