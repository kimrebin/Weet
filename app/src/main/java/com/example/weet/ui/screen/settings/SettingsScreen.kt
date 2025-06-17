package com.example.weet.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weet.data.local.SettingsDataStore
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(navController: NavController, onBack: () -> Unit) {
    var notificationInterval by remember { mutableStateOf(3) }
    var popupTime by remember { mutableStateOf("Not set") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)){
        Text("Notification Interval (days)")
        Slider(value = notificationInterval.toFloat(), onValueChange = {
            notificationInterval = it.toInt()
        }, valueRange = 1f..30f)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Popup Time: $popupTime")
        Button(onClick = {
            navController.navigate("schedule_popup")
        }) {
            Text("Set Popup Time")
        }

        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val settingDataStore = remember { SettingsDataStore(context) }
        Button(onClick = {
          scope.launch {
              settingDataStore.saveSettings(notificationInterval, popupTime)
          }
        }) {
            Text("Save Settings")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack){
            Text("Back")
        }
    }
}