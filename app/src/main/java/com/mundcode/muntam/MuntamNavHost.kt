package com.mundcode.muntam

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.presentation.ui.main.MainScreen

@Composable
fun MuntamNavHost(
    navController: NavHostController,
    startDestination: MutamDestination,
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
            MainScreen()
        }
    }
}
