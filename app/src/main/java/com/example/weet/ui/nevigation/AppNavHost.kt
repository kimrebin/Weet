package com.example.weet.ui.nevigation

import androidx.collection.intListOf
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weet.data.local.SettingsKeys
import com.example.weet.ui.screen.add.AddPersonScreen
import com.example.weet.ui.screen.auth.AuthScreen
import com.example.weet.ui.screen.checklist.ChecklistScreen
import com.example.weet.ui.screen.main.MainScreen
import com.example.weet.ui.screen.profile.ProfileScreen


@Composable
fun AppNavHost(navController: NavHostController, isAuthenticated: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "main" else "auth"
    ){
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("main"){
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(
                onPersonClick = { personId ->
                    navController.navigate("profile/$personId")
                },
                onAddPerson = {
                    navController.navigate("add")
                },
                onOpenChecklist = {
                    navController.navigate("checklist")
                },
                onOpenSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("profile/{personId}") {backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId") ?: return@composable
            ProfileScreen(
                personId = personId,
                onBack = {navController.popBackStack()}
            )
        }

        composable("add"){
            AddPersonScreen (
                onPersonAdded = {navController.popBackStack()}
            )
        }

        composable("checklist") {
            ChecklistScreen(
                onClose = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = {navController.popBackStack()}
            )
        }
    }
}
