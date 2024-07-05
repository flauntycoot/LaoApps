package com.example.laoapps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.laoapps.R
import com.example.laoapps.ui.theme.LaoBackG

@Composable
fun SettingsScreen(navController: NavController) {
    Surface(
        color = LaoBackG
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LanguageSettings()
            Divider()
            ContactInformation()
            Divider()
            // UpdateFunctionality()
        }
    }
}
    @Composable
    fun LanguageSettings() {
        var selectedLanguage by remember { mutableStateOf("English") }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Language Settings", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Language selection dropdown
            DropdownMenu(
                expanded = false, // Toggle this as per requirement
                onDismissRequest = { /* Handle dismiss if needed */ }
            ) {
                DropdownMenuItem(onClick = { selectedLanguage = "English" }) {
                    Text("English")
                }
                DropdownMenuItem(onClick = { selectedLanguage = "Russian" }) {
                    Text("Russian")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Selected Language: $selectedLanguage", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
        }
    }

@Composable
fun ContactInformation() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Contact Information", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
        Spacer(modifier = Modifier.height(16.dp))

        // QR Code image (Replace with actual QR code image)
        Image(
            painter = painterResource(id = R.drawable.qr_code1),
            contentDescription = "QR Code",
            modifier = Modifier.align(AbsoluteAlignment.Left).fillMaxSize(0.2f)


        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email and phone number
        Text("Email: laocars@gmail.com", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Phone: +9017772277", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
        Spacer(modifier = Modifier.height(18.dp))
        Text("Beta Ver.0.3", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,)
    }
}
/*
@Composable
fun UpdateFunctionality() {
    // Replace with actual logic to check for updates
    val isNewVersionAvailable = true
    val scope = rememberCoroutineScope()
    val openUrl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    if (isNewVersionAvailable) {
        Button(
            onClick = {
                // Replace with logic to initiate app update
                // Example: Launch a browser to download the latest version
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/latest-app.apk"))
                scope.launch {
                    openUrl.launch(intent)
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Install Latest Version")
        }
    }
}

 */