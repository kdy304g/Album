package com.example.album_player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var audioService: AlbumPlayerService? = null
    private var bound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AlbumPlayerService.AudioBinder
            audioService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            bound = false
        }
    }

    init {
        Intent(context, AlbumPlayerService::class.java).also { intent ->
            context.bindService(intent.setPackage(context.packageName), connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun getAudioService(): AlbumPlayerService? {
        return if (this.bound) {
            this.audioService
        } else {
            null
        }
    }
}