package com.example.laoapps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoGreen

@Composable
fun SettingsScreen(navController: NavHostController) {
    Surface(color = LaoBackG)
    {
        Row(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .padding(64.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .clickable { }
                    .padding(vertical = 64.dp, horizontal = 32.dp)

            ) {
                Icon(
                    Icons.Rounded.Place,
                    contentDescription = "Language settings",
                    tint = LaoGreen,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.5f)
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(text = "Language", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
                Text(text = "settings", style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)

            }
            //Divider()
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .clickable { }
                    .padding(vertical = 64.dp, horizontal = 32.dp)


            ) {
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = "Contact information",
                    tint = LaoGreen,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.5f)
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "Contact",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.5f)
                )
                Text(
                    text = "information",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.5f)
                )

            }
        }
    }
}