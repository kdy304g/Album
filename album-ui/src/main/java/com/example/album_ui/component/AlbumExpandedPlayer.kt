package com.example.album_ui.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.album_player.player.PlayerStates
import com.example.album_player.util.formatTime
import com.example.album_domain.model.Track
import com.example.album_player.player.PlaybackState
import com.example.album_player.player.PlayerEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AlbumExpandedPlayer(
    modifier: Modifier = Modifier,
    selectedTrack: Track,
    playerEvents: PlayerEvents,
    playbackState: StateFlow<PlaybackState>,
    playerStates: PlayerStates,
    volumeStateFlow: MutableStateFlow<Int>
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TrackInfo(
            trackImage = selectedTrack.trackImage,
            trackName = selectedTrack.trackName,
            artistName = selectedTrack.artistName,
            volumeStateFlow = volumeStateFlow
        )
        TrackProgressSlider(playbackState = playbackState) {
            playerEvents.onSeekBarPositionChanged(it)
        }
        TrackControls(
            onPreviousClick = playerEvents::onPreviousClick,
            onPlayPauseClick = playerEvents::onPlayPauseClick,
            onNextClick = playerEvents::onNextClick,
            playerStates = playerStates
        )
    }
}
@Composable
fun TrackInfo(
    trackImage: String,
    trackName: String,
    artistName: String,
    volumeStateFlow: MutableStateFlow<Int>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 350.dp)
    ) {
        TrackImage(
            trackImage = trackImage,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
        )
    }
    VolumeSlider(volumeStateFlow)
    Text(
        text = trackName,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
    Text(
        text = artistName,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    )
}

@Composable
fun VolumeSlider(
    volumeStateFlow: MutableStateFlow<Int>
) {
    val context = LocalContext.current
    val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    var volume by remember {
        mutableFloatStateOf(audioManager.getStreamVolume(STREAM_MUSIC).toFloat())
    }

    LaunchedEffect(Unit) {
        volumeStateFlow.collect {
            volume = it.toFloat()
        }
    }

    Slider(
        value = volume,
        onValueChange = { volume = it },
        onValueChangeFinished = {
            audioManager.setStreamVolume(STREAM_MUSIC, volume.toInt(), 0)
        },
        valueRange = 0f..audioManager.getStreamMaxVolume(STREAM_MUSIC).toFloat(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        colors = customSliderColors()
    )
}

@Composable
fun TrackProgressSlider(
    playbackState: StateFlow<PlaybackState>,
    onSeekBarPositionChanged: (Long) -> Unit
) {
    val playbackStateValue: PlaybackState = playbackState.collectAsState(
        initial = PlaybackState(0L, 0L)
    ).value
    var currentMediaProgress = playbackStateValue.currentPlaybackPosition.toFloat()
    var currentPosTemp by rememberSaveable { mutableFloatStateOf(0f) }

    Slider(
        value = if (currentPosTemp == 0f) currentMediaProgress else currentPosTemp,
        onValueChange = { currentPosTemp = it },
        onValueChangeFinished = {
            currentMediaProgress = currentPosTemp
            currentPosTemp = 0f
            onSeekBarPositionChanged(currentMediaProgress.toLong())
        },
        valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat().coerceAtLeast(0f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = playbackStateValue.currentPlaybackPosition.formatTime(),
        )
        Text(
            text = playbackStateValue.currentTrackDuration.formatTime(),
        )
    }
}

@Composable
fun TrackControls(
    playerStates: PlayerStates,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviousIcon(onClick = onPreviousClick, isBottomTab = false)
        PlayPauseIcon(
            onClick = onPlayPauseClick,
            playerStates = playerStates
        )
        NextIcon(onClick = onNextClick)
    }
}

@Composable
private fun customSliderColors(): SliderColors = SliderDefaults.colors(
    activeTickColor = Color.Transparent,
    inactiveTickColor = Color.Transparent,
    inactiveTrackColor = Color.LightGray,
    activeTrackColor = Color.Blue,
    thumbColor = Color.Blue
)