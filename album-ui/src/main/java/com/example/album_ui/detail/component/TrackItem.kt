package com.example.album_ui.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.album_core.utils.conditional
import com.example.album_domain.model.Album
import com.example.album_player.player.AlbumPlayer

@Composable
fun TrackItem(
    modifier: Modifier = Modifier,
    index: Int,
    album: Album
) {
    val track = album.tracks[index]
    Row(
        modifier = modifier
            .fillMaxWidth()
            .conditional(index == album.tracks.size - 1) {
                clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            }
            .background(Color.LightGray)
            .padding(vertical = 10.dp)
            .clickable {
                AlbumPlayer.onTrackClick(album.tracks.indexOf(track), album.tracks)
            }
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = (index + 1).toString(),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = album.tracks[index].trackName,
            color = Color.Black
        )
    }
    if (index < album.tracks.size - 1) Divider()
}