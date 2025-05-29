package com.example.weet.ui.screen.checklist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChecklistScreen(onClose: () -> Unit) {
    var recentContact by remember {mutableStateOf(false)}
    var emotionalStatus by remember {mutableStateOf(0)}

    Column (
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recent Contect?")
        Switch(checked = recentContact, onCheckedChange = {recentContact = it})
        Spacer(modifier = Modifier.height(16.dp))

        Text("Emotional Status")
        Slider(value = emotionalStatus.toFloat(), onValueChange = {
            emotionalStatus = it.toInt()
        }, valueRange = 0f..10f)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onClose()
        }){
            Text("Submit")
        }
    }
}