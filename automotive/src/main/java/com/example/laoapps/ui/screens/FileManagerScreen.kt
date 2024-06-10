package com.example.laoapps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laoapps.ui.theme.LaoBackG

@Composable
fun FileManagerScreen(navController: NavHostController) {
    Surface(
        color = LaoBackG
    ) {
        Row {

            // Simulated file manager data
            val files = listOf("System App 1", "User App 1", "System App 2", "User App 2")
            var showSystemApps by remember { mutableStateOf(true) }
            var showUserApps by remember { mutableStateOf(true) }

            Column(modifier = Modifier.fillMaxSize()) {
                Row {
                    Checkbox(checked = showSystemApps, onCheckedChange = { showSystemApps = it })
                    Text("System Apps",style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
                    Spacer(modifier = Modifier.width(16.dp))
                    Checkbox(checked = showUserApps, onCheckedChange = { showUserApps = it })
                    Text("User Apps",style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    )
                }
                LazyColumn {
                    items(files.size) { index ->
                        val file = files[index]
                        if ((showSystemApps && file.startsWith("System")) || (showUserApps && file.startsWith(
                                "User"
                            ))
                        ) {
                            Text(file, modifier = Modifier.clickable { /* Handle click */ },style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
                        }
                    }
                }
            }
        }
    }
}