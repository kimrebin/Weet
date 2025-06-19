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
import androidx.compose.ui.platform.LocalContext
import com.example.weet.ui.screen.popup.ChecklistPopup
import com.example.weet.viewmodel.ChecklistViewModel


@Composable
fun ProfileScreen(
    personId: Int?,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val photoUrl by viewModel.photoUrl.collectAsState()
    val name by viewModel.name.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val historyMessage by viewModel.historyMessage.collectAsState()
    val score by viewModel.relationshipScore.collectAsState()

    val checklistViewModel: ChecklistViewModel = hiltViewModel()
    var showChecklistPopup by remember { mutableStateOf(false) }
    var popupTime by remember { mutableStateOf("") }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updatePhotoUrl(it.toString()) }
    }

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

        // ✅ 이미지 선택 영역
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

        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = relationship,
            onValueChange = { viewModel.updateRelationship(it) },
            label = { Text("Tag (e.g. Family, School)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = historyMessage,
            onValueChange = { viewModel.updateHistoryMessage(it) },
            label = { Text("Memo") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ScoreDonut(score = score)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.savePerson(score) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { showChecklistPopup = true },
                enabled = personId != null,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Checklist")
            }
        }
    }

    // ✅ ChecklistPopup 연결
    if (showChecklistPopup && personId != null) {
        val tagWeight = when (relationship.lowercase()) {
            "family" -> 1.2
            "friend" -> 1.0
            "business" -> 0.8
            else -> 1.0
        }

        ChecklistPopup(
            personId = personId,
            personTagWeight = tagWeight,
            popupTime = popupTime,
            onPopupTimeChange = { popupTime = it },
            context = context,
            viewModel = checklistViewModel,
            onDismiss = { showChecklistPopup = false }
        )
    }
}
