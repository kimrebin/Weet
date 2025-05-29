package com.example.weet.ui.screen.add

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddPersonScreen(onPersonAdded: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = name, onValueChange = {name = it}, label = {Text(text = "Name")})
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = tag, onValueChange = {tag = it}, label = {Text("tag")})
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            //TODO: Save to Firestore
            onPersonAdded()
        }) {
            Text("Add Person")
        }
    }
}