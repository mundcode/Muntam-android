package com.mundcode.muntam.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.FavoriteQuestions
import com.mundcode.muntam.MuntamBottomDestination
import com.mundcode.muntam.Settings
import com.mundcode.muntam.Subjects
import com.mundcode.muntam.presentation.screen.main.favorites.FavoriteQuestionsScreen
import com.mundcode.muntam.presentation.screen.main.settings.SettingsScreen
import com.mundcode.muntam.presentation.screen.main.subjects.SubjectsScreen

@Composable
fun MainBottomNavHost(
    navController: NavHostController,
    startDestination: MuntamBottomDestination = Subjects,
    modifier: Modifier,
    onNavOutEvent: (route: String) -> Unit
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination.route,
    ) {
        composable(
            route = Subjects.route
        ) {
            SubjectsScreen(
                onNavOutEvent = { route ->
                    onNavOutEvent(route)
                }
            )
        }

        composable(
            route = FavoriteQuestions.route
        ) {
            FavoriteQuestionsScreen()
        }

        composable(
            route = Settings.route
        ) {
            SettingsScreen()
        }
    }
}
