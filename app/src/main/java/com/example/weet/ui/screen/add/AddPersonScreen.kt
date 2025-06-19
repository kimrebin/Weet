package com.example.weet.ui.screen.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weet.viewmodel.AddPersonViewModel

@Composable
fun AddPersonScreen(
    onPersonAdded: () -> Unit,
    viewModel: AddPersonViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }

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

        OutlinedTextField(
            value = tag,
            onValueChange = { tag = it },
            label = { Text("Tag") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && tag.isNotBlank()) {
                    viewModel.addPerson(name, tag)
                    onPersonAdded()
                }
            },
            enabled = name.isNotBlank() && tag.isNotBlank()
        ) {
            Text("Add Person")
        }
    }
}