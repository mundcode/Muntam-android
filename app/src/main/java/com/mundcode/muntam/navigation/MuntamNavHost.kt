package com.mundcode.muntam.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.presentation.screen.exam_record.ExamRecordScreen
import com.mundcode.muntam.presentation.screen.exams.ExamsScreen
import com.mundcode.muntam.presentation.screen.main.MainScreen
import com.mundcode.muntam.presentation.screen.main.navigateSingleTopTo
import com.mundcode.muntam.presentation.screen.questions.QuestionScreen
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
        ) {
            SubjectModifyScreen(
                onClickBack = {
                    navController.popBackStack(
                        route = SubjectModify.routeWithArgs,
                        inclusive = true,
                        saveState = false
                    )
                }
            )
        }

        composable(
            route = Exams.routeWithArgs,
            arguments = Exams.arguments
        ) {
            ExamsScreen(
                onNavEvent = { route ->
                    navController.navigate(route) {
                        popUpTo(route) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onClickBack = {
                    navController.popBackStack(
                        route = Exams.routeWithArgs,
                        inclusive = true,
                        saveState = false
                    )
                }
            )
        }

        composable(
            route = ExamRecord.routeWithArgs,
            arguments = ExamRecord.arguments
        ) {
            ExamRecordScreen(
                onNavEvent = { route ->
                    navController.navigate(route) {
                        popUpTo(ExamRecord.routeWithArgs) {
                            inclusive = true
                        }
                    }
                },
                onBackEvent = {
                    navController.popBackStack(
                        route = ExamRecord.routeWithArgs,
                        inclusive = true,
                        saveState = false
                    )
                }
            )
        }

        composable(
            route = Questions.routeWithArgs,
            arguments = Questions.arguments
        ) {
            QuestionScreen(
                onBackEvent = {
                    navController.popBackStack(
                        route = Questions.routeWithArgs,
                        inclusive = true,
                        saveState = false
                    )
                }
            )
        }

        composable(
            route = SubjectSettings.route
        ) {
            SubjectSettingsScreen()
        }

        composable(
            route = QuestionDetails.route
        ) {
            // todo 첫 배포 이후
        }

        composable(
            route = QuestionSettings.route
        ) {
            // todo
        }
    }
}
