package com.mundcode.muntam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.presentation.model.BottomSheetModel
import com.mundcode.muntam.presentation.screen.main.favorites.FavoriteQuestionsScreen
import com.mundcode.muntam.presentation.screen.main.settings.SettingsScreen
import com.mundcode.muntam.presentation.screen.main.subjects.SubjectsScreen

@Composable
fun MainBottomNavHost(
    navController: NavHostController,
    startDestination: MuntamBottomDestination = Subjects,
    modifier: Modifier,
    onNavOutEvent: (route: String) -> Unit,
    onBottomSheetEvent: (BottomSheetModel) -> Unit
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
                },
                onBottomSheetEvent = onBottomSheetEvent
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
