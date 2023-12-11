package com.example.album_ui.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AlbumTopAppBar(
    canNavigateBack: Boolean,
    backgroundColor: Color,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = backgroundColor,
        title = {
            Text(
                text = "Album App",
                color = Color.White
            )
        },
        navigationIcon = if (canNavigateBack) {
            {
                IconButton(
                    onClick = { onNavigationIconClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        } else {
            null
        }
    )
}