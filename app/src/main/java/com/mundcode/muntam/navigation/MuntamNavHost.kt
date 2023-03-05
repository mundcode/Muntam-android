package com.mundcode.muntam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.presentation.screen.exams.ExamsScreen
import com.mundcode.muntam.presentation.screen.main.MainScreen
import com.mundcode.muntam.presentation.screen.main.navigateSingleTopTo
import com.mundcode.muntam.presentation.screen.subject_add.SubjectAddScreen
import com.mundcode.muntam.presentation.screen.subject_modify.SubjectModifyScreen
import com.mundcode.muntam.presentation.screen.subject_setting.SubjectSettingsScreen

@Composable
fun MuntamNavHost(
    navController: NavHostController,
    startDestination: MuntamDestination = Main,
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
                navController.navigateSingleTopTo(route)
            }
        }

        composable(
            route = SubjectAdd.route
        ) {
            SubjectAddScreen(
                onClickBack = {
                    navController.popBackStack(
                        route = SubjectAdd.route,
                        inclusive = true,
                        saveState = false
                    )
                }
            )
        }

        composable(
            route = SubjectModify.routeWithArgs,
            arguments = SubjectModify.arguments
        ) { navBackStackEntry ->
            val subjectId =
                navBackStackEntry.arguments?.getInt(Exams.subjectIdArg) ?: return@composable
            SubjectModifyScreen()
        }

        composable(
            route = Exams.routeWithArgs,
            arguments = Exams.arguments
        ) { navBackStackEntry ->
            val subjectId =
                navBackStackEntry.arguments?.getInt(Exams.subjectIdArg) ?: return@composable
            ExamsScreen(subjectId) { route ->
                navController.navigate(route) {
                    popUpTo(route) {
                        saveState = true
                    }
                    restoreState = true
                }
            }
        }

        composable(
            route = SubjectSettings.route
        ) {
            SubjectSettingsScreen()
        }

        composable(
            route = ExamRecord.route
        ) {
            // todo
        }

        composable(
            route = Questions.route
        ) {
            // todo
        }

        composable(
            route = QuestionDetails.route
        ) {
        }

        composable(
            route = QuestionSettings.route
        ) {
            // todo
        }
    }
}
