package com.example.album_player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.MoreExecutors

private const val NOTIFICATION_ID = 200
const val NOTIFICATION_CHANNEL_ID = "notification channel id"

class AlbumNotificationManager(
    val context: Context, val player: Player,
    private val notificationListener: PlayerNotificationManager.NotificationListener
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNotificationService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession
    ) {
        createNotificationChannel(context)
        buildNotification(mediaSession)
        startForegroundNotification(mediaSessionService)
    }

    @OptIn(UnstableApi::class)
    private fun buildNotification(mediaSession: MediaSession) {
        val sessionToken =
            SessionToken(context, ComponentName(context, AlbumPlayerService::class.java))

        val controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
        }, MoreExecutors.directExecutor())

        val notificationManager =
            PlayerNotificationManager.Builder(context, 111, NOTIFICATION_CHANNEL_ID)
                .setChannelImportance(NotificationUtil.IMPORTANCE_HIGH)
                .setMediaDescriptionAdapter(audioDescriptor)
                .setNotificationListener(notificationListener)
                .build()


        notificationManager.setMediaSessionToken(mediaSession.sessionCompatToken)
        notificationManager.setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.setUseRewindAction(false)
        notificationManager.setUseFastForwardAction(false)
        notificationManager.setUsePreviousAction(true)
        notificationManager.setUsePlayPauseActions(true)
        notificationManager.setUseNextAction(true)
        notificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        notificationManager.setPlayer(player)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundNotification(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setOngoing(true)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    private val audioDescriptor =
        @UnstableApi object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return player.currentMediaItem?.mediaMetadata?.albumTitle ?: "album title"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                return pendingIntent()
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return ""
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
            val bitmapDrawable: BitmapDrawable =
                ContextCompat.getDrawable(
                    context,
                    androidx.media3.ui.R.drawable.exo_notification_play
                ) as BitmapDrawable
            return bitmapDrawable.bitmap
            }
        }

    private fun pendingIntent(): PendingIntent? {
        val sessionActivityPendingIntent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(context, 0, sessionIntent, PendingIntent.FLAG_IMMUTABLE)
            }
        return sessionActivityPendingIntent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val currentChannel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)

        val channelName = "Album Player"
        val channelDescription = "Album Channel"

        val newChannel = when {
            currentChannel == null -> {
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = channelDescription
                    setShowBadge(false)
                }
            }

            currentChannel.name != channelName || currentChannel.description != channelDescription || currentChannel.canShowBadge() -> {
                currentChannel.apply {
                    name = channelName
                    description = channelDescription
                    setShowBadge(false)
                }
            }

            else -> null
        }

        if (newChannel != null) {
            notificationManager.createNotificationChannel(newChannel)
        }

    }
}