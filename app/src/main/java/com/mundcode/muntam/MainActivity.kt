package com.mundcode.muntam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.mundcode.designsystem.theme.DarkColorPalette
import com.mundcode.designsystem.theme.LightColorPalette
import com.mundcode.muntam.navigation.MuntamNavHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeAdmob()

        setContent {
            MuntamApp()
        }
    }

    private fun initializeAdmob() {
        MobileAds.initialize(this) {}
    }
}


@Composable
fun MuntamApp() {
    MuntamTheme(
        darkTheme = false // todo 다크테마 적용시 삭제
    ) {
        val navController = rememberNavController()
        val currentBackstack by navController.currentBackStackEntryAsState()

        Scaffold { innerPadding ->
            MuntamNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun MuntamTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = DarkColorPalette.background
        )
        DarkColorPalette
    } else {
        systemUiController.setSystemBarsColor(
            color = LightColorPalette.background
        )
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
