package com.example.weet.ui.screen.profile

import com.example.weet.ui.components.ScoreDonut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weet.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    personId: Int,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadPerson(personId)
    }

    val name by viewModel.name.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val historyMessage by viewModel.historyMessage.collectAsState()
    val score by viewModel.relationshipScore.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Profile Page", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // 이름 입력
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 관계 입력 (간단히 텍스트로 구현 — 나중에 Dropdown으로 교체 가능)
        OutlinedTextField(
            value = relationship,
            onValueChange = { viewModel.updateRelationship(it) },
            label = { Text("Relationship") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 히스토리 메시지 입력
        OutlinedTextField(
            value = historyMessage,
            onValueChange = { viewModel.updateHistoryMessage(it) },
            label = { Text("History Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 점수 donut (임시)
        ScoreDonut(score = score)

        Spacer(modifier = Modifier.height(24.dp))

        // 저장 버튼
        Button(
            onClick = { viewModel.savePerson(score) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save")
        }
    }
}
