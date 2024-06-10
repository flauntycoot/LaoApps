package com.example.laoapps.ui.navigation

import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laoapps.composablefiles.data.filesystems.LocalFileSystem
import com.example.laoapps.composablefiles.ui.components.DirectoryComponent
import com.example.laoapps.ui.screens.AuthorizationScreen
import com.example.laoapps.ui.screens.FileManagerScreen
import com.example.laoapps.ui.screens.HomeScreen
import com.example.laoapps.ui.screens.LoadingScreen
import com.example.laoapps.ui.screens.MarketScreen
import com.example.laoapps.ui.screens.SettingsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main",

    ) {
        composable("loading") { LoadingScreen(onNavigateToAuthorization = { navController.navigate("authorization") } ) }
        composable("authorization") { AuthorizationScreen(onNavigateToHomeScreen = { navController.navigate("home") })}
        composable("home") { HomeScreen(navController) }
        composable("market") { MarketScreen(navController) }
        composable("directory") { DirectoryComponent(Environment.getExternalStorageDirectory().absolutePath, LocalFileSystem())}
        composable("file_manager") { FileManagerScreen(navController) }
        composable("settings") {
            SettingsScreen(
                navController = navController
            )
        }    }
}
