package com.example.weet.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weet.viewmodel.ProfileViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val name by viewModel.name.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val historyMessage by viewModel.historyMessage.collectAsState()
    val score = 87

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Relationship Score 원형 표시
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = score / 100f,
                modifier = Modifier.size(80.dp),
                strokeWidth = 6.dp
            )
            Text("$score", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // Add Image 영역
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("+ Add Image", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이름 입력
        OutlinedTextField(
            value = name,
            onValueChange = viewModel::updateName,
            label = { Text("Name") }
        )

        // 관계 입력
        OutlinedTextField(
            value = relationship,
            onValueChange = viewModel::updateRelationship,
            label = { Text("Relationship") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 히스토리 입력
        Text("History", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = historyMessage,
            onValueChange = viewModel::updateHistoryMessage,
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // 저장 버튼
        Button(onClick = { viewModel.savePerson() }) {
            Text("Save")
        }
    }
}

