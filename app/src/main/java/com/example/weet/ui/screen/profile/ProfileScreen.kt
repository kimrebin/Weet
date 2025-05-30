package com.example.weet.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileScreen(personId: Int) {
    Column {
        Text("Profile for person ID: $personId")
    }
}
