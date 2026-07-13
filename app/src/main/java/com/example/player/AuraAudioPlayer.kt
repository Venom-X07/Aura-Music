package com.example.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.MainActivity
import com.example.data.model.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Collections

@OptIn(UnstableApi::class)
class AuraAudioPlayer(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private val _currentSongIndex = MutableStateFlow(-1)
    val currentSongIndex: StateFlow<Int> = _currentSongIndex.asStateFlow()

    // Sleep Timer remaining seconds state
    private val _sleepTimerSeconds = MutableStateFlow(0)
    val sleepTimerSeconds: StateFlow<Int> = _sleepTimerSeconds.asStateFlow()

    private var sleepTimerJob: Job? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Equalizer State Simulation (applied on visualizer)
    val bassBoost = MutableStateFlow(0f)
    val trebleBoost = MutableStateFlow(0f)
    val eqBands = MutableStateFlow(floatArrayOf(0f, 0f, 0f, 0f, 0f)) // 60Hz, 230Hz, 910Hz, 4kHz, 14kHz

    private val positionTicker = object : Runnable {
        override fun run() {
            exoPlayer?.let { player ->
                if (player.isPlaying) {
                    _playbackPosition.value = player.currentPosition
                }
            }
            mainHandler.postDelayed(this, 300)
        }
    }

    init {
        mainHandler.post {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    _isPlaying.value = playing
                    if (playing) {
                        mainHandler.post(positionTicker)
                    } else {
                        mainHandler.removeCallbacks(positionTicker)
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    val index = currentMediaItemIndex
                    _currentSongIndex.value = index
                    if (index in _queue.value.indices) {
                        _currentSong.value = _queue.value[index]
                        _playbackPosition.value = 0L
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    if (state == Player.STATE_ENDED) {
                        // Handle manual transition or loop
                    }
                }
            })
        }
        getMediaSession()
    }

    fun getMediaSession(): MediaSession? {
        if (mediaSession == null && exoPlayer != null) {
            try {
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = android.app.PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT
                )
                mediaSession = MediaSession.Builder(context, exoPlayer!!)
                    .setSessionActivity(pendingIntent)
                    .build()
            } catch (e: Exception) {
                Log.e("AuraAudioPlayer", "Failed to build MediaSession", e)
            }
        }
        return mediaSession
    }

    fun getExoPlayer(): ExoPlayer? = exoPlayer

    private fun startPlaybackService() {
        try {
            val intent = Intent(context, PlaybackService::class.java)
            context.startService(intent)
        } catch (e: Exception) {
            Log.e("AuraAudioPlayer", "Failed to start PlaybackService", e)
        }
    }

    fun setQueue(songs: List<Song>, startIndex: Int = 0) {
        startPlaybackService()
        mainHandler.post {
            initializePlayer()
            val player = exoPlayer ?: return@post

            player.stop()
            player.clearMediaItems()

            _queue.value = songs
            _currentSongIndex.value = startIndex

            val mediaItems = songs.map { song ->
                val metadata = MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artist)
                    .setAlbumTitle(song.album)
                    .setArtworkUri(song.albumArtUri?.let { Uri.parse(it) })
                    .build()

                MediaItem.Builder()
                    .setUri(Uri.parse(song.path))
                    .setMediaId(song.id.toString())
                    .setMediaMetadata(metadata)
                    .build()
            }

            player.setMediaItems(mediaItems)
            if (startIndex in songs.indices) {
                player.seekTo(startIndex, 0L)
                _currentSong.value = songs[startIndex]
            }
            player.prepare()
            player.play()
        }
    }

    fun play() {
        startPlaybackService()
        mainHandler.post { exoPlayer?.play() }
    }

    fun pause() {
        mainHandler.post { exoPlayer?.pause() }
    }

    fun togglePlayPause() {
        mainHandler.post {
            val player = exoPlayer ?: return@post
            if (player.isPlaying) {
                player.pause()
            } else {
                startPlaybackService()
                player.play()
            }
        }
    }

    fun seekTo(positionMs: Long) {
        mainHandler.post {
            exoPlayer?.seekTo(positionMs)
            _playbackPosition.value = positionMs
        }
    }

    fun skipToNext() {
        mainHandler.post {
            val player = exoPlayer ?: return@post
            if (player.hasNextMediaItem()) {
                player.seekToNext()
            } else if (_queue.value.isNotEmpty()) {
                player.seekTo(0, 0L) // Wrap around
            }
        }
    }

    fun skipToPrevious() {
        mainHandler.post {
            val player = exoPlayer ?: return@post
            if (player.currentPosition > 5000L) {
                player.seekTo(0L) // Restart current song
            } else if (player.hasPreviousMediaItem()) {
                player.seekToPrevious()
            } else if (_queue.value.isNotEmpty()) {
                player.seekTo(_queue.value.size - 1, 0L) // Wrap to last
            }
        }
    }

    fun setShuffle(enabled: Boolean) {
        mainHandler.post {
            val player = exoPlayer ?: return@post
            player.shuffleModeEnabled = enabled
            _shuffleEnabled.value = enabled
        }
    }

    fun setRepeatMode(mode: Int) {
        mainHandler.post {
            val player = exoPlayer ?: return@post
            player.repeatMode = mode
            _repeatMode.value = mode
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        mainHandler.post {
            val player = exoPlayer ?: return@post
            player.setPlaybackSpeed(speed)
            _playbackSpeed.value = speed
        }
    }

    // Queue Modifiers
    fun addToQueue(song: Song) {
        mainHandler.post {
            val currentList = _queue.value.toMutableList()
            if (!currentList.contains(song)) {
                currentList.add(song)
                _queue.value = currentList

                val metadata = MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artist)
                    .setAlbumTitle(song.album)
                    .build()

                val item = MediaItem.Builder()
                    .setUri(Uri.parse(song.path))
                    .setMediaId(song.id.toString())
                    .setMediaMetadata(metadata)
                    .build()

                exoPlayer?.addMediaItem(item)
            }
        }
    }

    fun removeFromQueue(songId: Long) {
        mainHandler.post {
            val currentList = _queue.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == songId }
            if (index != -1) {
                currentList.removeAt(index)
                _queue.value = currentList
                exoPlayer?.removeMediaItem(index)
            }
        }
    }

    fun moveQueueItem(fromIndex: Int, toIndex: Int) {
        mainHandler.post {
            val currentList = _queue.value.toMutableList()
            if (fromIndex in currentList.indices && toIndex in currentList.indices) {
                Collections.swap(currentList, fromIndex, toIndex)
                _queue.value = currentList
                exoPlayer?.moveMediaItem(fromIndex, toIndex)
            }
        }
    }

    fun clearQueue() {
        mainHandler.post {
            exoPlayer?.clearMediaItems()
            _queue.value = emptyList()
            _currentSong.value = null
            _currentSongIndex.value = -1
            _playbackPosition.value = 0L
            try {
                context.stopService(Intent(context, PlaybackService::class.java))
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        _sleepTimerSeconds.value = minutes * 60

        if (minutes <= 0) return

        sleepTimerJob = playerScope.launch {
            while (_sleepTimerSeconds.value > 0) {
                delay(1000L)
                _sleepTimerSeconds.value -= 1
            }
            pause()
        }
    }

    fun stopSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimerSeconds.value = 0
    }

    fun release() {
        mainHandler.removeCallbacks(positionTicker)
        try {
            mediaSession?.release()
            mediaSession = null
        } catch (e: Exception) {
            Log.e("AuraAudioPlayer", "Error releasing mediaSession", e)
        }
        exoPlayer?.release()
        exoPlayer = null
        sleepTimerJob?.cancel()
    }
}
