package com.example.laoapps.data

import android.content.Context
import android.os.Environment
import com.google.firebase.storage.FirebaseStorage
import java.io.File

fun downloadFileFromGoogleDrive(
    context: Context,
    downloadUrl: String,
    onProgress: (Int) -> Unit,
    onDownloadComplete: (Boolean, String?) -> Unit
) {
    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUrl)
    val localFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "downloaded_apk.apk")
    storageRef.getFile(localFile).addOnSuccessListener {
        onDownloadComplete(true, localFile.absolutePath)
    }.addOnFailureListener { e ->
        onDownloadComplete(false, e.message)
    }.addOnProgressListener { taskSnapshot ->
        val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
        onProgress(progress)
    }
}
