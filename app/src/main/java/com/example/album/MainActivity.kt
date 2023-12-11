package com.example.album

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.example.album.ui.theme.AlbumTheme
import com.example.album.ui.theme.PurpleGrey80
import com.example.album.volume.VOLUME_CHANGED_ACTION
import com.example.album.volume.VolumeChangeListener
import com.example.album_player.player.AlbumPlayer
import com.example.album_ui.AlbumApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var volumeChangeListener: VolumeChangeListener? = null
    private val volumeStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("Range")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlbumPlayer.connect(applicationContext)
        setUpVolumeListener()
        lifecycleScope

        setContent {
            AlbumTheme {
                AlbumApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                    backgroundColor = PurpleGrey80,
                    volumeStateFlow = volumeStateFlow
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(volumeChangeListener)
    }

    private fun setUpVolumeListener() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volumeChangeListener = VolumeChangeListener(
            audioManager, lifecycleScope, volumeStateFlow
        )
        val intentFilter = IntentFilter(VOLUME_CHANGED_ACTION)
        registerReceiver(volumeChangeListener, intentFilter)
    }

}