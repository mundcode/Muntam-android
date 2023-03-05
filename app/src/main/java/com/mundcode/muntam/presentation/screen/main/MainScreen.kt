package com.mundcode.muntam.presentation.screen.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mundcode.designsystem.components.bottomsheets.MTBottomSheets
import com.mundcode.designsystem.components.bottomsheets.option.SubjectOptionBottomSheetContent
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTBottomSheetBackground
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.muntam.navigation.Main
import com.mundcode.muntam.navigation.MainBottomNavHost
import com.mundcode.muntam.presentation.model.BottomSheetModel

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
    var bottomSheetState by remember {
        mutableStateOf<BottomSheetModel>(BottomSheetModel.None)
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
            },
            onBottomSheetEvent = {
                bottomSheetState = it
            }
        )
    }

    BottomSheetScreen(bottomSheetState, onCloseEvent = { bottomSheetState = BottomSheetModel.None })
}

@Composable
fun MuntamBottomNavigation(
    currentDestination: NavDestination?,
    onNavInEvent: (route: String) -> Unit
) {
    Column {
        Divider(modifier = Modifier.fillMaxWidth(), color = Gray200)
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            Main.screens.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (isSelected) {
                                    screen.selectedIcon
                                } else {
                                    screen.unselectedIcon
                                }
                            ),
                            contentDescription = screen.javaClass.simpleName
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        onNavInEvent(screen.route)
                    },
                    label = {
                        Text(text = screen.display, style = MTTextStyle.text10, color = Gray900)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomSheetScreen(bottomSheetState: BottomSheetModel, onCloseEvent: () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val show = bottomSheetState !is BottomSheetModel.None
    val backgroundColor by animateColorAsState(
        targetValue = if (show) {
            MTBottomSheetBackground
        } else {
            MaterialTheme.colors.background
        }
    )

    systemUiController.setStatusBarColor(color = backgroundColor)

    MTBottomSheets(
        show = show,
        onClickOutSide = {}
    ) {
        when (bottomSheetState) {
            is BottomSheetModel.SubjectMoreBottomSheet -> {
                SubjectOptionBottomSheetContent(
                    onClickClose = onCloseEvent,
                    onClickDelete = bottomSheetState.onClickDelete,
                    onClickModify = bottomSheetState.onClickModify
                )
            }
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
