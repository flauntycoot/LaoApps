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
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import coil.compose.rememberAsyncImagePainter
import com.example.laoapps.ui.theme.LaoBackG
import com.example.laoapps.ui.theme.LaoGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlin.math.pow

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

    // Authenticate Firebase
    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val scope = rememberCoroutineScope()
                scope.launch {
                    fetchAppData(
                        context = context,
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
            } else {
                errorMessageState.value = "Authentication failed: ${task.exception?.message}"
                isLoadingState.value = false
            }
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
fun MarketScreenContent(
    isLoading: Boolean,
    errorMessage: String?,
    apps: List<AppInfo>,
    context: Context
) {
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(apps) { appInfo ->
            AppItem(
                appInfo = appInfo,
                onDownloadClick = { url ->
                    val directDownloadUrl = convertToDirectDownloadUrl(url)
                    startDownload(context, directDownloadUrl, appInfo.name)
                    Log.d("MarketScreen", "Download clicked for: $url")
                },
                context = context
            )
        }
    }
}

suspend fun fetchAppData(
    context: Context,
    onSuccess: (List<AppInfo>) -> Unit,
    onError: (String) -> Unit,
    maxRetries: Int = 3
) {
    val db = FirebaseFirestore.getInstance()
    var attempt = 0

    suspend fun fetch() {
        try {
            val result = db.collection("marketApps").get().await()
            val appList = result.map { document ->
                document.toObject(AppInfo::class.java)
            }
            onSuccess(appList)
        } catch (exception: Exception) {
            if (attempt < maxRetries) {
                attempt++
                val delayTime = (2.0.pow(attempt.toDouble()) * 1000L).toLong()
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
    var httpsUrl by remember { mutableStateOf<String?>(null) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Convert gs:// URL to https:// URL
    LaunchedEffect(appInfo.iconUrl) {
        scope.launch {
            if (appInfo.iconUrl.startsWith("gs://")) {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.getReferenceFromUrl(appInfo.iconUrl)
                try {
                    val uri = storageRef.downloadUrl.await()
                    httpsUrl = uri.toString()
                    Log.d("AppItem", "Converted gs:// URL to: $httpsUrl")
                } catch (e: Exception) {
                    Log.e("AppItem", "Failed to get download URL for ${appInfo.iconUrl}", e)
                }
            } else {
                httpsUrl = appInfo.iconUrl
                Log.d("AppItem", "Using direct URL: $httpsUrl")
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = true }
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (httpsUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(httpsUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(128.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Text("Loading icon...", modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier
            .width(8.dp)
            .height(16.dp))
        Text(
            text = appInfo.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth()
        )
        if (isDownloading) {
            CircularProgressIndicator(
                progress = downloadProgress / 100f,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("$downloadProgress%")
        } else {
            Button(onClick = {
                onDownloadClick(appInfo.downloadUrl)
                isDownloading = true
                scope.launch {
                    monitorDownloadProgress(context, appInfo.downloadUrl) { progress ->
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

fun convertToDirectDownloadUrl(url: String): String {
    return if (url.startsWith("https://drive.google.com")) {
        val fileId = url.substringAfter("/d/").substringBefore("/view")
        "https://drive.google.com/uc?export=download&id=$fileId"
    } else {
        url
    }
}

fun startDownload(context: Context, url: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle("Downloading $fileName")
        .setDescription("Downloading ${Uri.parse(url).lastPathSegment}")
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            Uri.parse(url).lastPathSegment
        )
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
                val fileUri: Uri = FileProvider.getUriForFile(
                    context!!,
                    "${context.packageName}.provider",
                    File(uri.path!!)
                )

                val installIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(fileUri, "application/vnd.android.package-archive")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(installIntent)
            }
        }
    }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
}

@SuppressLint("Range")
suspend fun monitorDownloadProgress(
    context: Context,
    url: String,
    onProgressUpdate: (Int) -> Unit
) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle("Downloading APK")
        .setDescription("Downloading ${Uri.parse(url).lastPathSegment}")
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            Uri.parse(url).lastPathSegment
        )
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

    val downloadId = downloadManager.enqueue(request)
    var isDownloading = true

    while (isDownloading) {
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            if (status == DownloadManager.STATUS_FAILED) {
                val reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                Log.e("MarketScreen", "Download failed with reason: $reason")
                isDownloading = false
                break
            }
            val bytesDownloaded =
                cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytesTotal =
                cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
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
