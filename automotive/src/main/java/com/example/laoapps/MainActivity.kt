package com.example.laoapps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.laoapps.ui.screens.MainScreen
import com.example.laoapps.ui.theme.LaoAppsTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onPermissionsGranted()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
              onActivityResult.launch(
                  Intent(
                      Settings.ACTION_WIFI_ADD_NETWORKS, //adb shell appops set --uid PACKAGE_NAME MANAGE_EXTERNAL_STORAGE allow
             Uri.parse("package:$packageName")))
            } else {
                this.onPermissionsGranted()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            this.onPermissionsGranted()
        }
    }

    private val onActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                this.onPermissionsGranted()
            } else {
                this.onPermissionsRejected()
            }
        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                this.onPermissionsGranted()
            } else {
                this.onPermissionsRejected()
            }
        }

    private fun onPermissionsGranted() {
        setContent {
            LaoAppsTheme {
                LaoApps()
            }
        }
    }

    private fun onPermissionsRejected() {
        Toast.makeText(
            this,
            "This app requires external storage permissions to function.",
            Toast.LENGTH_LONG
        ).show()
        this.finishAndRemoveTask()
    }
}

@Composable
fun LaoApps() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
