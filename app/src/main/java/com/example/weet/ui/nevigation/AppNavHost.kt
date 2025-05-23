package com.example.weet.ui.nevigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.relationshiptracker.ui.screen.main.MainScreen
import com.example.relationshiptracker.ui.screen.profile.ProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("profile/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            id?.let { ProfileScreen(personId = it) }
        }
    }
}
