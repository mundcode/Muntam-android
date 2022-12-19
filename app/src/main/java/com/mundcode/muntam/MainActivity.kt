package com.mundcode.muntam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mundcode.muntam.ui.theme.MuntamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuntamApp()
        }
    }
}

@Composable
fun MuntamApp() {
    MuntamTheme {
        val navController = rememberNavController()
        val currentBackstack by navController.currentBackStackEntryAsState()
        val currentDestination by remember {
            derivedStateOf {
                currentBackstack?.destination
            }
        }

        Scaffold { innerPadding ->
            MuntamNavHost(
                navController = navController,
                startDestination = Main,
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
}
