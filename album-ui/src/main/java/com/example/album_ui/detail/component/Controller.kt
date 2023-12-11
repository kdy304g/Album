package com.example.album_ui.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.ui.R
import com.example.album_domain.model.Album
import com.example.album_player.player.AlbumPlayer

@Composable
fun Controller(
    album: Album
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue)
                .clickable {
                    AlbumPlayer.onTrackClick(0, album.tracks)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.exo_icon_play),
                contentDescription = null
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue)
                .clickable {
                    AlbumPlayer.onShuffleClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.exo_icon_shuffle_on),
                contentDescription = null
            )
        }
    }
}