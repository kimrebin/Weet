package com.example.weet.ui.nevigation

import androidx.collection.intListOf
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weet.data.local.SettingsKeys
import com.example.weet.ui.screen.add.AddPersonScreen
import com.example.weet.ui.screen.checklist.ChecklistScreen
import com.example.weet.ui.screen.main.MainScreen
import com.example.weet.ui.screen.profile.ProfileScreen
import com.example.weet.ui.screen.settings.SettingsScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ){


        composable("main") {
            MainScreen(
                onPersonClick = { personId ->
                    navController.navigate("profile/$personId")
                },
                onAddPerson = {
                    navController.navigate("profile")
                },
                onOpenChecklist = {
                    navController.navigate("checklist")
                }
                /*onOpenSettings = {
                    navController.navigate("settings")
                }*/
            )
        }

        composable("profile/{personId}") {backStackEntry ->
            val personId = backStackEntry.arguments?.getInt("personId") ?: return@composable
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

/*        composable("settings") {
            SettingsScreen(
                navController = navController,
                onBack = {navController.popBackStack()}
            )*/
        }



}
