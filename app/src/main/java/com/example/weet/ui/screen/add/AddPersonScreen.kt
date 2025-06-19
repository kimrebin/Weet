package com.example.weet.ui.screen.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

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
                    viewModel.addPerson(name, selectedTag, historyMessage)
                    onPersonAdded()
                }
            },
            enabled = name.isNotBlank()
        ) {
            Text("Add Person")
        }
    }
}
