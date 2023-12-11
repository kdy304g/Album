package com.example.album_player

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumPlayerService @Inject constructor() : MediaSessionService() {

    private var notificationManager: AlbumNotificationManager? = null
    private lateinit var mediaSession: MediaSession

    private val audioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(this@AlbumPlayerService.audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }
    }

    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

    inner class AudioBinder : Binder() {
        fun getService(): AlbumPlayerService = this@AlbumPlayerService
    }

    private val binder = AudioBinder()
    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return false
    }

    @OptIn(UnstableApi::class) @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        notificationManager =
            AlbumNotificationManager(this, exoPlayer, notificationListener)

        mediaSession = MediaSession.Builder(this, exoPlayer)
            .build()

        pendingIntent()?.let { mediaSession.setSessionActivity(it) }

        notificationManager?.startNotificationService(
            mediaSessionService = this,
            mediaSession = mediaSession
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
        mediaSession.release()
    }

    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        @OptIn(UnstableApi::class)
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            if (exoPlayer.isPlaying) {
                exoPlayer.stop()
                exoPlayer.release()
            }
        }

        @OptIn(UnstableApi::class)
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            startForeground(notificationId, notification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    private fun pendingIntent(): PendingIntent? {
        val sessionActivityPendingIntent =
            packageManager.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, PendingIntent.FLAG_IMMUTABLE)
            }
        return sessionActivityPendingIntent
    }

    fun selectTrack(index: Int) {
        if (exoPlayer.playbackState == Player.STATE_IDLE) exoPlayer.prepare()
        exoPlayer.seekTo(index, 0)
        exoPlayer.playWhenReady = true
    }

    fun playPause() {
        when (exoPlayer.playbackState) {
            Player.STATE_IDLE -> {
                exoPlayer.prepare()
                exoPlayer.playWhenReady = !exoPlayer.playWhenReady
            }

            Player.STATE_ENDED -> {
                exoPlayer.seekTo(0)
                exoPlayer.play()
            }

            else -> exoPlayer.playWhenReady = !exoPlayer.playWhenReady
        }

    }

    fun shuffle() {
        exoPlayer.shuffleModeEnabled = !exoPlayer.shuffleModeEnabled
    }

    fun seekToPosition(position: Long) {
        scope.launch {
            exoPlayer.seekTo(position)
        }
    }

}
