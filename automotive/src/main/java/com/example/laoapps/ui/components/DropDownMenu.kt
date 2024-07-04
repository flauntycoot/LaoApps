package com.example.laoapps.ui.components

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laoapps.ui.theme.LaoSecondary
import com.example.laoapps.ui.theme.LaoWhite

@Composable
fun DropDownMenuItem(
    text: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier.background(color = LaoSecondary).fillMaxHeight() // Background color for the dropdown item
    ) {
        Text(
            text = text,
            color = LaoWhite, // Text color set to white
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}
