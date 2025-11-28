package com.example.collegeeventapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signin") {

        composable("signin") {
            SignInScreen(navController)
        }

        composable("signup") {
            SignUpScreen(navController)
        }

        // Main bottom nav entry -> home list
        composable("home") {
            HomeScreen(
                goToAddScreen = { navController.navigate("add") },
                goToDetail = { id -> navController.navigate("detail/$id") },
                navController = navController
            )
        }

        composable("add") {
            AddEventScreen(goBack = { navController.popBackStack() })
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("id") ?: ""
            DetailEventScreen(
                id = eventId,
                goBack = { navController.popBackStack() },
                goEdit = { navController.navigate("edit/$eventId") }
            )
        }

        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("id") ?: ""
            EditEventScreen(
                id = eventId,
                goBack = { navController.popBackStack() }
            )
        }
    }
}
