package com.anand.catchandroidassessment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anand.catchandroidassessment.view.DetailScreen
import com.anand.catchandroidassessment.view.ListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            // no more jsonString parameter
            ListScreen(navController = navController)
        }
        composable("detail/{title}/{subtitle}/{description}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val subtitle = backStackEntry.arguments?.getString("subtitle") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            DetailScreen(
                title       = title,
                subtitle    = subtitle,
                description = description,
                navController = navController
            )
        }
    }
}
