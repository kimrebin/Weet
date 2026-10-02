package com.example.weet.ui.screen.add

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weet.viewmodel.AddPersonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonScreen(
    onPersonAdded: () -> Unit,
    viewModel: AddPersonViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    val tagOptions = listOf("family", "friend", "business", "other")
    var expanded by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf(tagOptions.first()) }
    var historyMessage by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    if (photoUri != null) {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("+ Add Image")
                    }
                }
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 태그 드롭다운
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedTag,
                onValueChange = {},
                label = { Text("Tag") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tagOptions.forEach { tag ->
                    DropdownMenuItem(
                        text = { Text(tag) },
                        onClick = {
                            selectedTag = tag
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 메모 입력
        OutlinedTextField(
            value = historyMessage,
            onValueChange = { historyMessage = it },
            label = { Text("Memo") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && selectedTag.isNotBlank()) {
                    viewModel.addPerson(
                        name = name,
                        tag = selectedTag,
                        historyMessage = historyMessage,
                        photoUrl = photoUri?.toString() ?: ""
                    )
                    onPersonAdded()
                }
            },
            enabled = name.isNotBlank()
        ) {
            Text("Add Person")
        }
    }
}
