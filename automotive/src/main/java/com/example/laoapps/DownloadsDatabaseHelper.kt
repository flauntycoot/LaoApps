package com.example.laoapps

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.laoapps.data.DownloadInfo

class DownloadsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "downloads.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "downloads"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_URL = "url"
        private const val COLUMN_FILE_NAME = "file_name"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_VERSION = "version"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_URL TEXT," +
                "$COLUMN_FILE_NAME TEXT," +
                "$COLUMN_STATUS TEXT," +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_VERSION INTEGER)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllDownloads(): List<DownloadInfo> {
        val downloads = mutableListOf<DownloadInfo>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
                val fileName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_NAME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val version = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VERSION))

                downloads.add(DownloadInfo(id, url, fileName, status, date, version))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return downloads
    }

    fun insertDownload(downloadInfo: DownloadInfo): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_URL, downloadInfo.url)
            put(COLUMN_FILE_NAME, downloadInfo.fileName)
            put(COLUMN_STATUS, downloadInfo.status)
            put(COLUMN_DATE, downloadInfo.date)
            put(COLUMN_VERSION, downloadInfo.version)
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return newRowId
    }
}
