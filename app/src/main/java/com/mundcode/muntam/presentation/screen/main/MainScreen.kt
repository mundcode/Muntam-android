package com.mundcode.muntam.presentation.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mundcode.muntam.Main

@Composable
fun MainScreen(
    onNavOutEvent: (route: String) -> Unit
) {
    val bottomNavController = rememberNavController()
    val currentBackstack by bottomNavController.currentBackStackEntryAsState()
    val currentDestination by remember {
        derivedStateOf {
            currentBackstack?.destination
        }
    }

    Scaffold(
        bottomBar = {
            MuntamBottomNavigation(
                currentDestination = currentDestination,
                onNavInEvent = { route ->
                    bottomNavController.navigateSingleTopTo(route)
                }
            )
        }
    ) { innerPadding ->
        MainBottomNavHost(
            navController = bottomNavController,
            modifier = Modifier.padding(innerPadding),
            onNavOutEvent = { route ->
                onNavOutEvent(route)
            }
        )
    }
}

@Composable
fun MuntamBottomNavigation(
    currentDestination: NavDestination?,
    onNavInEvent: (route: String) -> Unit
) {
    BottomNavigation {
        Main.screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.javaClass.simpleName
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    onNavInEvent(screen.route)
                }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
