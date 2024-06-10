package com.example.laoapps.ui.screens

import android.os.Environment
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laoapps.composablefiles.data.filesystems.LocalFileSystem
import com.example.laoapps.composablefiles.ui.components.DirectoryComponent
import com.example.laoapps.ui.components.NavigationDrawer

@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
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
                enterTransition = { //fadeIn(animationSpec = tween(700)) +
                        slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(200)
                ) },
                exitTransition = { //fadeOut(animationSpec = tween(700)) +
                        slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(200)
                ) },
                popEnterTransition= { //fadeIn(animationSpec = tween(700)) +
                        slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(200)
                ) },
                popExitTransition= { //fadeOut(animationSpec = tween(700)) +
                        slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(200)
                ) },
                modifier = Modifier.fillMaxSize()
            ) {
                composable("loading") { LoadingScreen(onNavigateToAuthorization = { navController.navigate("authorization") } ) }
                composable("authorization") { AuthorizationScreen(onNavigateToHomeScreen = { navController.navigate("home") })}
                composable("home") { HomeScreen(NavController) }
                composable("market") { MarketScreen(NavController) }
               // composable("file_manager") { FileManagerScreen(NavController) }
                composable("settings") { SettingsScreen(NavController) }
                composable("main") { MainScreen(navController) }
                composable("directory") { DirectoryComponent(Environment.getExternalStorageDirectory().absolutePath, LocalFileSystem())}


            }
        }
    }
}
