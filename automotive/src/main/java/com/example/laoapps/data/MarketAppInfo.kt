package com.example.laoapps.data

import android.graphics.drawable.Drawable

data class MarketAppInfo(
    val name: String,
    val icon: Drawable
)

data class DownloadInfo(
    val id: Long,
    val url: String,
    val fileName: String,
    val status: String,
    val date: String,
    val version: Int
)