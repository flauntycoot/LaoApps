package com.example.laoapps.ui.screens

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.pow

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            fetchAppData(
                onSuccess = { appList ->
                    appsState.value = appList
                    isLoadingState.value = false
                    Log.d("MarketScreen", "Fetched apps: $appList")
                },
                onError = { error ->
                    errorMessageState.value = error
                    isLoadingState.value = false
                    Log.e("MarketScreen", "Error fetching apps: $error")
                }
            )
        }
    }

    MarketScreenContent(
        isLoading = isLoadingState.value,
        errorMessage = errorMessageState.value,
        apps = appsState.value,
        context = context
    )
}

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

suspend fun fetchAppData(onSuccess: (List<AppInfo>) -> Unit, onError: (String) -> Unit, maxRetries: Int = 3) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        expectSuccess = true
        engine {
            requestTimeout = 60000
            endpoint {
                connectTimeout = 60000
                socketTimeout = 60000
            }
        }
    }

    val apiToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCIsImtpZCI6IjFrYnhacFJNQGJSI0tSbE1xS1lqIn0.eyJ1c2VyIjoiamsxMDc3NyIsInR5cGUiOiJhcGlfa2V5IiwicG9ydGFsX3Rva2VuIjoiNjM5MmIwOWQtZGFkZS00NDgwLThhNmEtNTM2ZWM3NzBkMDE2IiwiYXBpX2tleV9pZCI6ImZmZmVhNDEwLTUyOTMtNDQxMS05ODJhLTY1ZDI0ZjMzYjcxMiIsImlhdCI6MTcyMTA0MTMxM30.llRJ85fCtG8GWutn6FYYaHG4hQ7MpT1u5JI4TsbZDR_tsAjDX5XjmpEgr1qcL189NwFh6sYXCEzVII6qymJgVzWE0f8UJqAWsbE2rnHXYZc1qStz6rX4XFfTvCZambVpL1usPXFhAbqBz0AKnag1GbA5FnXpGJJsASQlXsx1kh3-D_F6K9C2DLG_EUFXlsSapdW4aldwQv6R_fNFMtFIipqBhZlR8c-_bSYqyk46sCYbhJO1usIHw978hjlbSBXnrKUlV_m32g56pAiTC_pLwh1qXI_PYj0DI2G68xqfV17PlSQ3I5LIdQTmvmK458Y-OxHvII3eIArWESAxvACv39i-fYltIX84cphfetIgeVtqSSt_T7KPAcLFOFPk6CIyklw1MnqxZgglxB2HqgPu5Lht4BBL2OYSrH0Fr4PjPBL1zZPueOFhX_TdHVAhFjbFDsrMWATGLliQS_DlH3uwzf6Slz0Q1tthQGhQZnYoXPq0P-s7pvJq3Z70He0-S_h5"  // Replace with your actual API token

    var attempt = 0

    suspend fun fetch() {
        try {
            val response: HttpResponse = client.get("http://91.198.220.245:5432/LaoAppsDB") {
                headers {
                    append("Authorization", "Bearer $apiToken")
                }
            }
            val responseBody: String = response.bodyAsText()
            Log.d("MarketScreen", "Response: $responseBody")
            val appList: List<AppInfo> = Json.decodeFromString(responseBody)
            onSuccess(appList)
        } catch (exception: Exception) {
            Log.e("MarketScreen", "Error fetching data", exception)
            if (attempt < maxRetries) {
                attempt++
                val delayTime = (2.0.pow(attempt.toDouble()) * 1000L).toLong()
                Log.d("MarketScreen", "Retrying in $delayTime ms")
                delay(delayTime)
                fetch()
            } else {
                onError("Error fetching data after $attempt attempts: ${exception.message}")
            }
        }
    }

    fetch()
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
