package com.example.weet.ui.screen.popup

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog

@Composable
fun SchedulePopup(onConfirm: (Int) -> Unit, onDismiss: () -> Unit) {
    var timeText by remember { mutableStateOf("15") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onConfirm(timeText.toIntOrNull() ?: 15) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Set Reminder Time") },
        text = {
            OutlinedTextField(
                value = timeText,
                onValueChange = { timeText = it },
                label = { Text("Minutes") },
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    )
}
