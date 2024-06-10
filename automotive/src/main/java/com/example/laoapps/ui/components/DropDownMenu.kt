package com.example.laoapps.ui.components

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DropDownMenuItem(
    text: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(onClick = onClick) {
        Text(text)
    }
}
