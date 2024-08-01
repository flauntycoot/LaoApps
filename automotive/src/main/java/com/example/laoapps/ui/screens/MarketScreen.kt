package com.example.laoapps.ui.screens

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

@Serializable
data class AppInfo(
    val name: String = "",
    val iconUrl: String = "",
    val downloadUrl: String = ""
)

@Composable
fun MarketScreen(navController: NavController) {
    val appsState = remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    val errorMessageState = remember { mutableStateOf<String?>(null) }
    val isLoadingState = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val apps = fetchAppData()
                appsState.value = apps
                isLoadingState.value = false
            } catch (e: Exception) {
                errorMessageState.value = "Error fetching data: ${e.message}"
                isLoadingState.value = false
            }
        }
    }

    MarketScreenContent(
        isLoading = isLoadingState.value,
        errorMessage = errorMessageState.value,
        apps = appsState.value,
        context = LocalContext.current
    )
}




object DatabaseConfig {
    private const val URL = "jdbc:postgresql://91.198.220.245:5432/LaoAppsDB/public"
    private const val USER = "guest"
    private const val PASSWORD = "O#1\\?N%>*V-\\q6"

    fun connect(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}

fun fetchAppData(): List<AppInfo> {
    val connection = DatabaseConfig.connect()
    val apps = mutableListOf<AppInfo>()
    connection?.use {
        val statement = it.createStatement()
        val resultSet = statement.executeQuery("SELECT name, icon_url, download_url FROM apps")
        while (resultSet.next()) {
            val name = resultSet.getString("name")
            val iconUrl = resultSet.getString("icon_url")
            val downloadUrl = resultSet.getString("download_url")
            apps.add(AppInfo(name, iconUrl, downloadUrl))
        }
    }
    return apps
}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MarketScreenContent(isLoading: Boolean, errorMessage: String?, apps: List<AppInfo>, context: Context) {
    Surface(
        color = LaoBackG,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading -> LoadingIndicator()
            errorMessage != null -> ErrorMessage(errorMessage)
            else -> AppList(apps, context)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = LaoGreen,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun ErrorMessage(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colors.error,
        modifier = Modifier.padding(16.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppList(apps: List<AppInfo>, context: Context) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(apps) { appInfo ->
            AppItem(
                appInfo = appInfo,
                onDownloadClick = { url ->
                    startDownload(context, url, appInfo.name)
                    Log.d("MarketScreen", "Download clicked for: $url")
                },
                context = context
            )
        }
    }
}


@Composable
fun AppItem(appInfo: AppInfo, onDownloadClick: (String) -> Unit, context: Context) {
    var expanded by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = true }
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(appInfo.iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = appInfo.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth()
        )
        if (isDownloading) {
            CircularProgressIndicator(progress = downloadProgress / 100f, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("$downloadProgress%")
        } else {
            Button(onClick = {
                val downloadId = startDownload(context, appInfo.downloadUrl, appInfo.name)
                isDownloading = true
                scope.launch {
                    monitorDownloadProgress(context, downloadId) { progress ->
                        downloadProgress = progress
                        if (progress == 100) {
                            isDownloading = false
                        }
                    }
                }
            }) {
                Text("Download")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Add dropdown items here if needed
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun startDownload(context: Context, url: String, fileName: String): Long {
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle("Downloading $fileName")
        .setDescription("Downloading ${Uri.parse(url).lastPathSegment}")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Uri.parse(url).lastPathSegment)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)

    Log.d("MarketScreen", "Download started with ID: $downloadId")

    context.registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                Log.d("MarketScreen", "Download completed for ID: $downloadId")
                val uri: Uri = downloadManager.getUriForDownloadedFile(downloadId) ?: return
                val fileUri: Uri = FileProvider.getUriForFile(context!!, "${context.packageName}.provider", File(uri.path!!))

                val installIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(fileUri, "application/vnd.android.package-archive")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(installIntent)
            }
        }
    }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED)

    return downloadId
}

@SuppressLint("Range")
suspend fun monitorDownloadProgress(context: Context, downloadId: Long, onProgressUpdate: (Int) -> Unit) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query().setFilterById(downloadId)

    var isDownloading = true

    while (isDownloading) {
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            if (status == DownloadManager.STATUS_FAILED) {
                val reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                Log.e("MarketScreen", "Download failed with reason: $reason")
                isDownloading = false
                break
            }
            val bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            if (bytesTotal != 0) {
                val progress = ((bytesDownloaded * 100L) / bytesTotal).toInt()
                onProgressUpdate(progress)
                if (progress == 100) {
                    isDownloading = false
                }
            }
        }
        cursor.close()
        delay(500) // Update progress every 500ms
    }
}
