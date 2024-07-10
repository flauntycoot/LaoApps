package com.example.laoapps.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laoapps.data.HomeAppInfo
import com.example.laoapps.ui.components.DropDownMenuItem
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    var installedApps by remember { mutableStateOf(getInstalledApps(packageManager)) }

    Surface(
        color = LaoBackG
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            items(installedApps) { appInfo ->
                AppListItem(
                    appInfo = appInfo,
                    packageManager = packageManager,
                    onAppUninstalled = {
                        installedApps = getInstalledApps(packageManager)
                    }
                )
            }
        }
    }
}




@SuppressLint("SuspiciousIndentation")
@Composable
fun AppListItem(appInfo: HomeAppInfo, packageManager: PackageManager, onAppUninstalled: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mutableInteractionSource = remember {
        MutableInteractionSource()
    }
    val pressed = mutableInteractionSource.collectIsPressedAsState()
    val elevation = animateDpAsState(
        targetValue = if (pressed.value) {
            32.dp
        } else {
            8.dp
        },
        label = "elevation"
    )
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded = true }
                .fillMaxWidth(),
            //.graphicsLayer { this.shadowElevation = elevation.value.toPx() },

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Image(
                bitmap = appInfo.icon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(128.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier
                .height(16.dp))
            Text(

                text = appInfo.name,
               textAlign = TextAlign.Center,//.Justify,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()

                )
            DropdownMenu(
                modifier = Modifier.background(LaoSecondary),
                expanded = expanded,
                onDismissRequest = { expanded = false }


            ) {
                DropDownMenuItem(
                    text = "Open",
                    onClick = {
                        expanded = false
                        val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
                        launchIntent?.let { context.startActivity(it) }
                    }
                )
                DropDownMenuItem(
                    text = "Uninstall",
                    onClick = {
                        expanded = false
                        scope.launch {
                            val uri = Uri.parse("package:${appInfo.packageName}")
                            val intent = Intent(Intent.ACTION_DELETE, uri)
                            context.startActivity(intent)
                            // Wait for a moment to allow the uninstall process to complete
                            delay(2000)
                            onAppUninstalled()
                        }
                    }
                )
            }
        }
    }




fun getInstalledApps(packageManager: PackageManager): List<HomeAppInfo> {
    val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    return apps.filter {
        (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 // Exclude system apps
    }.map {
        val name = it.loadLabel(packageManager).toString()
        val drawable = it.loadIcon(packageManager)
        val bitmap = drawableToBitmap(drawable)
        val imageBitmap = bitmapToImageBitmap(bitmap)
        HomeAppInfo(name = name, packageName = it.packageName, icon = imageBitmap)
    }
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    } else {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}

fun bitmapToImageBitmap(bitmap: Bitmap): ImageBitmap {
    return bitmap.asImageBitmap()
}

