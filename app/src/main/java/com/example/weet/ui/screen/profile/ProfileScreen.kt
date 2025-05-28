package com.example.weet.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weet.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val name by viewModel.name.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val historyMessage by viewModel.historyMessage.collectAsState()
    val score by viewModel.relationshipScore.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 관계 점수 표시
        Text("Relationship Score", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = score / 100f,
                modifier = Modifier.size(80.dp),
                strokeWidth = 6.dp
            )
            Text("$score", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = interpretRQS(score),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        // 프로필 이미지 영역
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("+ Add Image", fontSize = 12.sp)
        }

        Spacer(Modifier.height(16.dp))

        // 이름 입력
        OutlinedTextField(
            value = name,
            onValueChange = viewModel::updateName,
            label = { Text("Name") }
        )

        Spacer(Modifier.height(16.dp))

        // 관계 선택
        Text("Relationship", fontWeight = FontWeight.Bold)
        Column {
            val options = listOf("가족", "친구", "직장", "애인")
            options.forEach { option ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = relationship == option,
                            onClick = {
                                viewModel.updateRelationship(option)
                                viewModel.updateTagWeight(getTagWeightFor(option))
                            }
                        )
                        .padding(4.dp)
                ) {
                    RadioButton(
                        selected = relationship == option,
                        onClick = {
                            viewModel.updateRelationship(option)
                            viewModel.updateTagWeight(getTagWeightFor(option))
                        }
                    )
                    Text(option, Modifier.padding(start = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 히스토리 메시지
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

fun interpretRQS(score: Int): String {
    return when (score) {
        in 80..100 -> "💚 매우 건강한 관계예요!"
        in 50..79 -> "😊 안정적이지만 개선 여지가 있어요"
        in 30..49 -> "😕 소원해지고 있어요"
        else -> "⚠️ 관계가 멀어지고 있어요. 대화가 필요해요"
    }
}

fun getTagWeightFor(relationship: String): Float {
    return when (relationship) {
        "가족" -> 1.2f
        "친구" -> 1.0f
        "직장" -> 0.8f
        "애인" -> 1.1f
        else -> 1.0f
    }
}
