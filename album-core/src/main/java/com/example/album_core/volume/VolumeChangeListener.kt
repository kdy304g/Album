package com.example.album_core.volume

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

const val VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION"
class VolumeChangeListener(
    private val audioManager: AudioManager,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val volumeStateFlow: MutableStateFlow<Int>
) : BroadcastReceiver() {

    private val streamType: Int = AudioManager.STREAM_MUSIC

    init {
        lifecycleScope.launch {
            volumeStateFlow.emit(
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            )
        }
    }
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == VOLUME_CHANGED_ACTION) {
                val volume: Int = audioManager.getStreamVolume(streamType)
                lifecycleScope.launch {
                    volumeStateFlow.emit(volume)
                }
        }
    }
}