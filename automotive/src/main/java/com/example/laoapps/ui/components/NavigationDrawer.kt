package com.example.laoapps.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.laoapps.ui.theme.LaoGreen
import com.example.laoapps.ui.theme.LaoSecondary

@Composable
fun MenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = LaoGreen,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp),
        )
    }

}
@Composable
fun NavigationDrawer(navController: NavController, modifier: Modifier = Modifier) {

    Surface(
        color = LaoSecondary,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
         //   .padding(8.dp)
            .zIndex(1f),        // Ensures the drawer is above the content
       // shape = MaterialTheme.shapes.medium

    ) {
        Column(
            verticalArrangement = Arrangement.Center, // Align items vertically at the center
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
              //  .padding(4.dp)
        ) {
            // Add items for navigation menu here
            MenuItem(
                text = "Home",
                icon = Icons.Rounded.Home,
                onClick = { navController.navigate("home")
                    Modifier.fillMaxSize(0.25f)
                }
            )
            MenuItem(
                text = "Market",
                icon = Icons.Rounded.ShoppingCart,
                onClick = { navController.navigate("market")
                    Modifier.fillMaxSize(0.25f)
                }
            )
            MenuItem(
                text = "File Manager",
                icon = Icons.Rounded.Phone,
                onClick = { navController.navigate("file_manager")
                    Modifier.fillMaxSize(0.25f)
                }
            )
            MenuItem(
                text = "Settings",
                icon = Icons.Rounded.Settings,
                onClick = { navController.navigate("settings")
                    Modifier.fillMaxSize(0.25f)
                }
            )
        }

    }
}






