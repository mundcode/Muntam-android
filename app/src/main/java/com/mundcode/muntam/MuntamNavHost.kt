package com.mundcode.muntam

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.presentation.ui.subject_setting.SubjectSettingsScreen
import com.mundcode.muntam.presentation.ui.exams.ExamsScreen
import com.mundcode.muntam.presentation.ui.main.MainScreen
import com.mundcode.muntam.presentation.ui.subject_add.SubjectAddScreen

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
            val subjectId =
                navBackStackEntry.arguments?.getInt(Exams.subjectIdArg) ?: return@composable
            ExamsScreen(subjectId)
        }

        composable(
            route = SubjectAdd.route
        ) {
            SubjectAddScreen()
        }

        composable(
            route = SubjectSettings.route
        ) {
            SubjectSettingsScreen()
        }

        composable(
            route = ExamRecord.route
        ) {

        }

        composable(
            route = Questions.route
        ) {

        }

        composable(
            route = QuestionDetails.route
        ) {

        }

        composable(
            route = QuestionSettings.route
        ) {

        }
    }
}
