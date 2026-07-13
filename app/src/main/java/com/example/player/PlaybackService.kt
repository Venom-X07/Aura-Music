package com.example.player

import android.content.Intent
import android.util.Log
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.AuraApplication

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("PlaybackService", "Service onCreate")
        try {
            val app = applicationContext as AuraApplication
            mediaSession = app.player.getMediaSession()
        } catch (e: Exception) {
            Log.e("PlaybackService", "Error initializing MediaSession in service", e)
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("PlaybackService", "onTaskRemoved")
        try {
            val app = applicationContext as AuraApplication
            val player = app.player.getExoPlayer()
            if (player != null && !player.playWhenReady) {
                stopSelf()
            }
        } catch (e: Exception) {
            Log.e("PlaybackService", "Error in onTaskRemoved", e)
        }
    }

    override fun onDestroy() {
        Log.d("PlaybackService", "Service onDestroy")
        mediaSession = null
        super.onDestroy()
    }
}
