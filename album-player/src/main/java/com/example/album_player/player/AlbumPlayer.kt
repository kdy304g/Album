package com.example.album_player.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.example.album_domain.model.Track
import com.example.album_player.AlbumPlayerService
import com.example.album_player.util.collectPlayerState
import com.example.album_player.util.launchPlaybackStateJob
import com.example.album_player.util.toMediaItemList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object AlbumPlayer : PlayerEvents {

    var service: AlbumPlayerService? = null

    val selectedTrackState: MutableSharedFlow<Track> = MutableSharedFlow()
    private val currentTracks = mutableStateListOf<Track>()
    private var currentTrackIndex: Int by mutableStateOf(-1)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var playbackStateJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState(0L, 0L))
    val playbackState: StateFlow<PlaybackState> get() = _playbackState

    val playerState = MutableStateFlow(PlayerStates.STATE_IDLE)
    val currentPlaybackPosition: Long
        get() = service?.exoPlayer?.currentPosition ?: 0L
    val currentTrackDuration: Long
        get() = service?.exoPlayer?.duration ?: 0L

    private val playerListener = PlayerListener()

    init {
        scope.collectPlayerState(this, AlbumPlayer::updateState)
    }

    fun connect(context: Context) {
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                AlbumPlayer.service =
                    (service as? AlbumPlayerService.AudioBinder)?.getService()?.apply {
                        exoPlayer.addListener(playerListener)
                    }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                service = null
            }
        }

        context.bindService(
            Intent(context, AlbumPlayerService::class.java).setPackage(context.packageName),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onPreviousClick() {
        service?.exoPlayer?.seekToPrevious()
        if (currentTrackIndex > 0) currentTrackIndex -= 1
    }

    override fun onNextClick() {
        service?.exoPlayer?.seekToNext()
        if (currentTracks.size - 1 > currentTrackIndex) currentTrackIndex += 1
    }

    override fun onPlayPauseClick() {
        service?.playPause()
    }

    override fun onTrackClick(index: Int, tracks: List<Track>) {
        currentTracks.clear()
        currentTracks.addAll(tracks)
        service?.exoPlayer?.setMediaItems(tracks.toMediaItemList())
        service?.exoPlayer?.prepare()
        currentTrackIndex = index
        service?.selectTrack(index)
    }

    fun onShuffleClick() {
        service?.shuffle()
    }

    override fun onSeekBarPositionChanged(position: Long) {
        service?.seekToPosition(position)
    }

    private fun updateState(state: PlayerStates) {
        if (currentTrackIndex != -1) {
            scope.launch {
                selectedTrackState.emit(currentTracks[currentTrackIndex])
            }
            updatePlaybackState(state)
        }
    }

    private fun updatePlaybackState(state: PlayerStates) {
        playbackStateJob?.cancel()
        playbackStateJob = scope.launchPlaybackStateJob(_playbackState, state, this)
    }

    private class PlayerListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    playerState.tryEmit(PlayerStates.STATE_IDLE)
                }

                Player.STATE_BUFFERING -> {
                    playerState.tryEmit(PlayerStates.STATE_BUFFERING)
                }

                Player.STATE_READY -> {
                    playerState.tryEmit(PlayerStates.STATE_READY)
                    if (service?.exoPlayer?.playWhenReady == true) {
                        playerState.tryEmit(PlayerStates.STATE_PLAYING)
                    } else {
                        playerState.tryEmit(PlayerStates.STATE_PAUSE)
                    }
                }

                Player.STATE_ENDED -> {
                    playerState.tryEmit(PlayerStates.STATE_END)
                }
            }
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (service?.exoPlayer?.playbackState == Player.STATE_READY) {
                if (playWhenReady) {
                    playerState.tryEmit(PlayerStates.STATE_PLAYING)
                } else {
                    playerState.tryEmit(PlayerStates.STATE_PAUSE)
                }
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                currentTrackIndex += 1
                scope.launch {
                    selectedTrackState.emit(currentTracks[currentTrackIndex])
                }
                playerState.tryEmit(PlayerStates.STATE_NEXT_TRACK)
                playerState.tryEmit(PlayerStates.STATE_PLAYING)
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            playerState.tryEmit(PlayerStates.STATE_ERROR)
        }
    }

}