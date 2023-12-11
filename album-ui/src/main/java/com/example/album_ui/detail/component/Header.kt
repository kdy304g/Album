package com.example.album_ui.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.album_domain.model.Album

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Header(
    album: Album
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GlideImage(
            model = album.albumArt,
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = album.title,
                color = Color.Black
            )
            Text(
                text = album.artist,
                color = Color.Gray
            )
        }
    }
}