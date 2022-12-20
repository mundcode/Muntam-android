package com.mundcode.muntam.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.*
import com.mundcode.muntam.presentation.ui.main.subjects.SubjectsScreen

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
//            FavoriteQuestionsScreen()
            Dummy(screenName = "FavoriteQuestions")

        }

        composable(
            route = Settings.route
        ) {
//            SettingsScreen()
            Dummy(screenName = "Settings")

        }
    }
}
