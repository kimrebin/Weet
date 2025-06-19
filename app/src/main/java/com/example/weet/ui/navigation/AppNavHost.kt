package com.example.weet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weet.ui.screen.add.AddPersonScreen
import com.example.weet.ui.screen.checklist.ChecklistScreen
import com.example.weet.ui.screen.main.MainScreen
import com.example.weet.ui.screen.profile.ProfileScreen
import com.example.weet.ui.screen.relationshipMap.RelationshipScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        composable("main") {
            MainScreen(
                onPersonClick = { personId -> navController.navigate("profile/$personId") },
                onAddPerson = { navController.navigate("addPerson") }, // ✅ 이 부분! (화진)
                onRelationshipClick = { navController.navigate("relationship") } // 추가 (규린)
            )
        }

        composable("addPerson") {
            AddPersonScreen(onPersonAdded = { navController.popBackStack() })
        }

        composable("profile/{personId}") {backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")?.toIntOrNull()
            if (personId == null){
                return@composable
            }
            ProfileScreen(
                personId = personId,
                onBack = {navController.popBackStack()},
                navController = navController
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

        composable("relationship") {
            RelationshipScreen(
                onBack = { navController.popBackStack() }
            )
        }


        /*        composable("settings") {
                    SettingsScreen(
                        navController = navController,
                        onBack = {navController.popBackStack()}
                    )*/
        }



}
