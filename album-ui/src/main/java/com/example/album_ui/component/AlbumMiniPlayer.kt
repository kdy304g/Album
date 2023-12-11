package com.example.album_ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.album_player.player.PlayerStates
import com.example.album_domain.model.Track
import com.example.album_player.player.PlayerEvents

@Composable
fun AlbumMiniPlayer(
    modifier: Modifier = Modifier,
    selectedTrack: Track,
    playerStates: PlayerStates,
    playerEvents: PlayerEvents,
    onBottomTabClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onBottomTabClick)
            .padding(all = 15.dp)
    ) {
        TrackImage(
            trackImage = selectedTrack.trackImage,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentSize()
        )
        TrackName(trackName = selectedTrack.trackName, modifier = Modifier.weight(1f))
        PreviousIcon(onClick = playerEvents::onPreviousClick, isBottomTab = true)
        PlayPauseIcon(
            playerStates = playerStates,
            onClick = playerEvents::onPlayPauseClick,
        )
        NextIcon(onClick = playerEvents::onNextClick)
    }
}