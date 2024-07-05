package com.example.laoapps.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laoapps.ui.components.NavigationDrawer

@Composable
fun MainScreen(navController: NavHostController) {
    val NavController = navController as NavHostController
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationDrawer(
            navController = NavController,
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .fillMaxHeight()
        )
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = NavController,
                startDestination = "loading",
                enterTransition = { fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(3000)) },
                popEnterTransition= { fadeIn(tween(300)) },
                popExitTransition= { fadeOut(tween(3000)) },
            ) {
                composable("loading") { LoadingScreen(onNavigateToAuthorization = { navController.navigate("authorization") } ) }
                composable("authorization") { AuthorizationScreen(onNavigateToHomeScreen = { navController.navigate("home") })}
                composable("home") { HomeScreen(NavController) }
                composable("market") { MarketScreen(navController) }
                composable("settings") { SettingsScreen(NavController) }
                composable("main") { MainScreen(navController) }
            }
        }
    }
}
