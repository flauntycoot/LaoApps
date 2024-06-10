package com.example.laoapps.composablefiles.data.navigation

import android.os.Parcelable

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.laoapps.composablefiles.data.FileSystemType
import com.example.laoapps.composablefiles.utils.urlEncode
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Location(val fileSystem: FileSystemType, val path: String) : Parcelable {
    val navUrl: String
        get() = "directory/${Json.encodeToString(this).urlEncode()}"
}