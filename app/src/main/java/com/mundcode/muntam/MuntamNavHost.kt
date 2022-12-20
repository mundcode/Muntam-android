package com.mundcode.muntam

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.presentation.ui.exams.ExamsScreen
import com.mundcode.muntam.presentation.ui.main.MainScreen

@Composable
fun MuntamNavHost(
    navController: NavHostController,
    startDestination: MutamDestination = Main,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination.route
    ) {
        composable(
            route = Main.route
        ) {
            MainScreen { route ->
                navController.navigate(route) {
                    popUpTo(Main.route) {
                        saveState = true
                    }
                }
            }
        }

        composable(
            route = Exams.routeWithArgs,
            arguments = Exams.arguments
        ) { navBackStackEntry ->
            val subjectId = navBackStackEntry.arguments?.getInt(Exams.subjectIdArg) ?: return@composable
            ExamsScreen(subjectId)
        }
    }
}
