package com.example.album_player.util

import androidx.media3.common.MediaItem
import com.example.album_player.player.PlayerStates
import com.example.album_domain.model.Track
import com.example.album_player.player.AlbumPlayer
import com.example.album_player.player.PlaybackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
fun List<Track>.toMediaItemList(): MutableList<MediaItem> {
    return this.map { MediaItem.fromUri(it.trackUrl) }.toMutableList()
}

fun CoroutineScope.collectPlayerState(
    myPlayer: AlbumPlayer, updateState: (PlayerStates) -> Unit
) {
    this.launch {
        myPlayer.playerState.collect {
            updateState(it)
        }
    }
}
fun CoroutineScope.launchPlaybackStateJob(
    playbackStateFlow: MutableStateFlow<PlaybackState>, state: PlayerStates, myPlayer: AlbumPlayer
) = launch {
    do {
        playbackStateFlow.emit(
            PlaybackState(
                currentPlaybackPosition = myPlayer.currentPlaybackPosition,
                currentTrackDuration = myPlayer.currentTrackDuration
            )
        )
        delay(1000) // delay for 1 second
    } while (state == PlayerStates.STATE_PLAYING && isActive)
}

fun Long.formatTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}