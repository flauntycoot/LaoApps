package com.example.laoapps.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun fetchAppInfo(): List<AppInfo> {
    val firestore = FirebaseFirestore.getInstance()
    val appList = mutableListOf<AppInfo>()
    try {
        val result = firestore.collection("marketApps").get().await()
        for (document in result) {
            val app = document.toObject(AppInfo::class.java)
            appList.add(app)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return appList
}
