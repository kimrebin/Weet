package com.example.weet.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weet.ui.components.ScoreDonut
import com.example.weet.viewmodel.ProfileViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.shape.CircleShape
import androidx.navigation.NavController



@Composable
fun ProfileScreen(
    personId: Int?,
    onBack: () -> Unit,
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(personId) {
        personId?.let { viewModel.loadPerson(it) }
    }

    val photoUrl by viewModel.photoUrl.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updatePhotoUrl(it.toString()) }
    }

    val name by viewModel.name.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val historyMessage by viewModel.historyMessage.collectAsState()
    val score by viewModel.relationshipScore.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 뒤로가기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 이미지 선택 영역
        Box(
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUrl.isNotBlank()) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("+ Add Image")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 이름
        OutlinedTextField(
            value = name,
            onValueChange = {}, // 변경 불가
            label = { Text("Name") },
            singleLine = true,
            readOnly = true,
            enabled = true, // UI 상 비활성화 (회색 처리)
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 태그
        OutlinedTextField(
            value = relationship,
            onValueChange = {}, // 변경 불가
            label = { Text("Tag") },
            singleLine = true,
            readOnly = true,
            enabled = true, // UI 상 비활성화
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 메모
        OutlinedTextField(
            value = historyMessage,
            onValueChange = { viewModel.updateHistoryMessage(it) },
            label = { Text("Memo") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        ScoreDonut(score = score)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.savePerson(score)
                navController.navigate("main") {
                    popUpTo("profile/{personId}") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp)
        ) {
            Text("Save")
        }
    }
}

