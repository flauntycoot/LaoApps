package com.example.laoapps.ui.screens

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.laoapps.R
import com.example.laoapps.data.MarketAppInfo
import com.example.laoapps.ui.components.DropDownMenuItem
import com.example.laoapps.ui.theme.LaoBackG

@Composable
fun MarketScreen(navController: NavHostController) {
    val context = LocalContext.current
    val marketApps = remember { getMarketApps(context) }
    Surface(
        color = LaoBackG
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(marketApps) { appInfo ->
                MarketAppItem(appInfo = appInfo)
            }
        }
    }
}
@Composable
fun MarketAppItem(appInfo: MarketAppInfo) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = true }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val bitmap = (appInfo.icon as BitmapDrawable).bitmap
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = appInfo.name,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropDownMenuItem(
                text = "Install",
                onClick = {
                    expanded = false
                    // Handle install logic here
                }
            )
        }
    }
}

fun getMarketApps(context: Context): List<MarketAppInfo> {
    return listOf(
        MarketAppInfo(
            name = "Example App 1",
            icon = ContextCompat.getDrawable(context, R.drawable.logo)!!
        ),
        MarketAppInfo(
            name = "Example App 2",
            icon = ContextCompat.getDrawable(context, R.drawable.logo)!!
        ),
        MarketAppInfo(
            name = "Example App 3",
            icon = ContextCompat.getDrawable(context, R.drawable.logo)!!
        ),
        MarketAppInfo(
            name = "Example App 4",
            icon = ContextCompat.getDrawable(context, R.drawable.logo)!!
        )
    )
}