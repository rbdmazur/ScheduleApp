package com.example.scheduleapp.mainscreen.barscontent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scheduleapp.mainscreen.MainViewModel
import com.example.scheduleapp.utils.DateFormats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: MainViewModel,
    onNavClick: () -> Unit
) {
    val dateState by viewModel.dateState.collectAsState()
    TopAppBar(
        title = {
            Text(
                text = DateFormats.barTitleFormat.format(dateState.dates[dateState.selectedDay]),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavClick() }) {
                Icon(Icons.Default.Menu, "")
            }
        }
    )
}