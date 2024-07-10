package com.example.laoapps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.laoapps.ui.theme.LaoGreen
import com.example.laoapps.ui.theme.LaoSecondary
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

data class MenuItemData(val text: String, val icon: ImageVector, val route: String)

@Composable
fun NavigationDrawer(navController: NavController, modifier: Modifier = Modifier) {
    val menuItems = listOf(
        MenuItemData("Home", Icons.Rounded.Home, "home"),
        MenuItemData("Market", Icons.Rounded.ShoppingCart, "market"),
        MenuItemData("Settings", Icons.Rounded.Settings, "settings")
    )

    Surface(
        color = LaoSecondary,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            .zIndex(1f) // Ensures the drawer is above the content
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(menuItems) { item ->
                MenuItem(
                    text = item.text,
                    icon = item.icon,
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MenuItem(text: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick.invoke() }
            .fillMaxSize()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = LaoGreen,
            modifier = Modifier
                .size(120.dp) // Further increase the size of the icon
                .padding(bottom = 8.dp)
        )
        Text(
            text = text,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
