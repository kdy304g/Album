package com.example.album_ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.album_player.player.PlayerStates

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackImage(
    trackImage: String,
    modifier: Modifier
) {
    GlideImage(
        model = trackImage,
        contentScale = ContentScale.FillHeight,
        contentDescription = null,
        modifier = modifier.clip(shape = RoundedCornerShape(8.dp))
    )
}

@Composable
fun TrackName(trackName: String, modifier: Modifier) {
    Text(
        text = trackName,
        color = Color.Black,
        modifier = modifier.padding(start = 16.dp, end = 8.dp)
    )
}

@Composable
fun PreviousIcon(onClick: () -> Unit, isBottomTab: Boolean) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = androidx.media3.ui.R.drawable.exo_icon_previous),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}
@Composable
fun PlayPauseIcon(onClick: () -> Unit, playerStates: PlayerStates) {
    if (playerStates == PlayerStates.STATE_BUFFERING) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size = 48.dp)
                .padding(all = 9.dp),
            color = Color.Black,
        )
    } else {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(
                    id = if (playerStates == PlayerStates.STATE_PLAYING) androidx.media3.ui.R.drawable.exo_icon_pause
                    else androidx.media3.ui.R.drawable.exo_icon_play
                ),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
@Composable
fun NextIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = androidx.media3.ui.R.drawable.exo_icon_next),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}