package com.example.laoapps.ui.screens

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.laoapps.DownloadsDatabaseHelper
import com.example.laoapps.data.DownloadInfo
import com.example.laoapps.ui.theme.LaoBackG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MarketScreen(navController: NavController) {
    val context = LocalContext.current
    val dbHelper = DownloadsDatabaseHelper(context)
    val downloads = remember { mutableStateOf(dbHelper.getAllDownloads()) }

    Surface(
        color = LaoBackG
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //items(*downloads.value.toTypedArray()) { downloadInfo ->
               // DownloadItem(downloadInfo = downloadInfo)
            }
        }
    }
//}
/*
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
*/


@SuppressLint("Range")
fun downloadAndInstallApk(context: Context, url: String, fileName: String) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(fileName)
        .setDescription("Downloading $fileName")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

    val downloadId = downloadManager.enqueue(request)

    CoroutineScope(Dispatchers.IO).launch {
        var downloading = true
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                    val uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    installApk(context, uriString)
                }
            }
            cursor.close()
        }
    }
}

fun installApk(context: Context, uriString: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.parse(uriString), "application/vnd.android.package-archive")
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}
@Composable
fun DownloadItem(downloadInfo: DownloadInfo, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                downloadAndInstallApk(context, downloadInfo.url, downloadInfo.fileName)
            }
            .padding(16.dp)
    ) {
        Text(text = downloadInfo.fileName, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = downloadInfo.status)
    }
}
