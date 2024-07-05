package com.example.laoapps.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.laoapps.R
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoGreen
import kotlinx.coroutines.delay


@Composable
fun LoadingScreen(onNavigateToAuthorization: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000) // Delay for 3 seconds
        onNavigateToAuthorization()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)  // Ensures the drawer is above the content
            .background(LaoBackG), // Dark background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Ensure you have a logo image in your resources
                contentDescription = "Logo",
                modifier = Modifier.size(350.dp) // Adjust the size as needed
            )

            // Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(
                color = LaoGreen, // White loading indicator for contrast
                modifier = Modifier.size(120.dp)
            )
        }
    }

}
