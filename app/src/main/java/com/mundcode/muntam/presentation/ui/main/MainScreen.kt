package com.mundcode.muntam.presentation.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mundcode.muntam.Main

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackstack by navController.currentBackStackEntryAsState()
    val currentDestination by remember {
        derivedStateOf {
            currentBackstack?.destination
        }
    }

    Scaffold { innerPadding ->
        MainNavHost(
            navController = navController,
            startDestination = Main,
            modifier = Modifier.padding(innerPadding),
            onNavOutEvent = {

            }
        )
    }
}