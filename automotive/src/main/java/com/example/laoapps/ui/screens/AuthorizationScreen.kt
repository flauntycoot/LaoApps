package com.example.laoapps.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.laoapps.R
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoWhite
import kotlinx.coroutines.delay

@Composable
fun AuthorizationScreen(onNavigateToHomeScreen: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000) // Delay for 3 seconds
        onNavigateToHomeScreen()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)  // Ensures the drawer is above the content
            .background(LaoBackG) // Dark background
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // QR Code 1
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.qr_code1), // Replace with your actual drawable resource
                    contentDescription = "Serial",
                    modifier = Modifier.size(200.dp) // Adjust size as needed
                )
                Text(
                    text = "Serial \n Scan the code and \n copy serial number",
                    color = LaoWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // QR Code 2
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.qr_code2), // Replace with your actual drawable resource
                    contentDescription = "Telegram",
                    modifier = Modifier.size(200.dp) // Adjust size as needed
                )
                Text(
                    text = "Telegram \n Send the serial \n number here",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Add more QR codes as needed
        }
    }
}