package com.example.laoapps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.laoapps.data.AppInfo
import com.example.laoapps.data.downloadFileFromGoogleDrive
import com.example.laoapps.data.fetchAppInfo
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoGreen
import kotlinx.coroutines.launch


@Composable
fun MarketScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var appList by remember { mutableStateOf(emptyList<AppInfo>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        appList = fetchAppInfo()
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().background(color = LaoBackG), color = LaoGreen)
    } else {
        Surface(
            color = LaoBackG
        ) {
            LazyVerticalGrid(
                columns = GridCells.FixedSize(256.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(128.dp),
            )
            {
                items(appList) { appInfo ->
                    AppItem(
                        appInfo = appInfo,
                        onDownloadClick = { downloadUrl ->
                            coroutineScope.launch {
                                downloadFileFromGoogleDrive(
                                    context,
                                    downloadUrl,
                                    { progress -> /* handle progress */ },
                                    { success, filePath ->
                                        if (success) {
                                            // Handle successful download, e.g., prompt to install
                                        } else {
                                            // Handle download failure
                                        }
                                    })
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppItem(appInfo: AppInfo, onDownloadClick: (String) -> Unit) {
    val progress by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = true }
            .fillMaxWidth(0.2f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Replace with actual logic to load image/icon
        Image(
            painter = rememberAsyncImagePainter(appInfo.iconUrl), // Assuming iconUrl is the URL to the icon
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(128.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier
            .width(8.dp)
            .height(32.dp))
        Text(
            text = appInfo.name,
            textAlign = TextAlign.Justify,
            style = com.example.laoapps.ui.theme.Typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { onDownloadClick(appInfo.downloadUrl) }) {
            Text("Download")
        }
        if (progress > 0) {
            LinearProgressIndicator(progress = progress / 100f)
            Text("$progress%")
        }

        // Additional content based on 'expanded' state
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Add dropdown items here if needed
        }
    }
}
