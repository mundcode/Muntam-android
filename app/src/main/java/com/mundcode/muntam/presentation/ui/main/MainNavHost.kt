package com.mundcode.muntam.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.FavoriteQuestions
import com.mundcode.muntam.MutamDestination
import com.mundcode.muntam.Settings
import com.mundcode.muntam.Subjects

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: MutamDestination,
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

        }

        composable(
            route = FavoriteQuestions.route
        ) {

        }

        composable(
            route = Settings.route
        ) {

        }
    }
}
