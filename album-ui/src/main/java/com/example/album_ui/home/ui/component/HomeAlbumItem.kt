package com.example.album_ui.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.album_domain.model.Album

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeAlbumItem(
    modifier: Modifier = Modifier,
    album: Album
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 10.dp
            )
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)

    ) {
        GlideImage(
            model = album.albumArt,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = album.title,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = album.artist,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(5.dp))
    }

}