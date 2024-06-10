package com.example.laoapps.composablefiles.ui.screens

import androidx.compose.runtime.Composable
import com.example.laoapps.composablefiles.data.FileSystem
import com.example.laoapps.composablefiles.ui.components.DirectoryComponent


@Composable
fun DirectoryScreen(path: String, fileSystem: FileSystem) {
    DirectoryComponent(path = path, fileSystem = fileSystem)
}
