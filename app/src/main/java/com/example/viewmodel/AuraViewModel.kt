package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.AuraApplication
import com.example.data.database.EqPresetEntity
import com.example.data.database.PlaylistEntity
import com.example.data.model.PlaybackStats
import com.example.data.model.Song
import com.example.data.preferences.AuraPreferences
import com.example.data.repository.MusicRepository
import com.example.player.AuraAudioPlayer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuraViewModel(
    private val repository: MusicRepository,
    private val player: AuraAudioPlayer,
    private val preferences: AuraPreferences
) : ViewModel() {

    // --- Media Store Songs & Playback ---
    val songs: StateFlow<List<Song>> = repository.songsFlow

    val favoriteIds: StateFlow<Set<Long>> = repository.favoriteIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val playlists: StateFlow<List<PlaylistEntity>> = repository.playlists
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<com.example.data.database.PlayHistoryEntity>> = repository.history
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<PlaybackStats?> = repository.getPlaybackStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentSong: StateFlow<Song?> = player.currentSong
    val isPlaying: StateFlow<Boolean> = player.isPlaying
    val playbackPosition: StateFlow<Long> = player.playbackPosition
    val playbackSpeed: StateFlow<Float> = player.playbackSpeed
    val shuffleEnabled: StateFlow<Boolean> = player.shuffleEnabled
    val repeatMode: StateFlow<Int> = player.repeatMode
    val queue: StateFlow<List<Song>> = player.queue
    val currentSongIndex: StateFlow<Int> = player.currentSongIndex
    val sleepTimerSeconds: StateFlow<Int> = player.sleepTimerSeconds

    // --- Search & Filter Queries ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeCategory = MutableStateFlow("All") // All, Songs, Albums, Artists, Folders
    val activeCategory: StateFlow<String> = _activeCategory.asStateFlow()

    // --- Equalizer State ---
    val customEqBands: StateFlow<FloatArray> = player.eqBands.asStateFlow()
    val bassBoost: StateFlow<Float> = player.bassBoost.asStateFlow()
    val trebleBoost: StateFlow<Float> = player.trebleBoost.asStateFlow()

    // --- Preferences bridge ---
    val themeMode: StateFlow<Int> = preferences.themeMode
    val accentColorHex: StateFlow<String> = preferences.accentColorHex
    val shakeToChange: StateFlow<Boolean> = preferences.shakeToChange
    val audioQuality: StateFlow<String> = preferences.audioQuality
    val crossfadeEnabled: StateFlow<Boolean> = preferences.crossfade
    val gaplessEnabled: StateFlow<Boolean> = preferences.gapless

    // --- Scanning / Loading State ---
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    init {
        // Automatically start scanning device or prepare demo on load
        scanDeviceMusic()

        // Register a listener for play changes to log play metrics to DB
        viewModelScope.launch {
            currentSong.collect { song ->
                song?.let {
                    repository.logSongPlay(it.id)
                }
            }
        }
    }

    fun scanDeviceMusic() {
        viewModelScope.launch {
            _isScanning.value = true
            try {
                repository.scanDeviceSongs()
            } catch (e: Exception) {
                // handle error
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun loadDemoTracks() {
        viewModelScope.launch {
            _isScanning.value = true
            repository.loadDemoTracks()
            _isScanning.value = false
        }
    }

    // --- Playback Commands ---
    fun playSong(song: Song, customQueue: List<Song>? = null) {
        val activeQueue = customQueue ?: songs.value
        val index = activeQueue.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        player.setQueue(activeQueue, index)
    }

    fun togglePlayPause() = player.togglePlayPause()
    fun skipToNext() = player.skipToNext()
    fun skipToPrevious() = player.skipToPrevious()
    fun seekTo(positionMs: Long) = player.seekTo(positionMs)
    fun toggleShuffle() = player.setShuffle(!shuffleEnabled.value)
    fun cycleRepeatMode() {
        val nextMode = when (repeatMode.value) {
            0 -> 1 // REPEAT_MODE_ONE (1)
            1 -> 2 // REPEAT_MODE_ALL (2)
            else -> 0 // REPEAT_MODE_OFF (0)
        }
        player.setRepeatMode(nextMode)
    }
    fun setPlaybackSpeed(speed: Float) = player.setPlaybackSpeed(speed)

    // --- Queue Management ---
    fun removeFromQueue(songId: Long) = player.removeFromQueue(songId)
    fun moveQueueItem(from: Int, to: Int) = player.moveQueueItem(from, to)
    fun clearQueue() = player.clearQueue()
    fun stopPlayback() = player.clearQueue()
    fun playNext(song: Song) {
        player.addToQueue(song)
    }

    // --- Favorites ---
    fun toggleFavorite(songId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(songId)
        }
    }

    // --- Playlists ---
    fun createPlaylist(name: String) {
        viewModelScope.launch {
            repository.createPlaylist(name)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            repository.deletePlaylist(playlistId)
        }
    }

    fun addSongToPlaylist(playlistId: Int, songId: Long) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun removeSongFromPlaylist(playlistId: Int, songId: Long) {
        viewModelScope.launch {
            repository.removeSongFromPlaylist(playlistId, songId)
        }
    }

    fun getSongsInPlaylist(playlistId: Int): Flow<List<Song>> {
        return repository.getSongsInPlaylistFlow(playlistId)
    }

    // --- Equalizer Tuning ---
    fun setEqBand(index: Int, gain: Float) {
        val current = player.eqBands.value.copyOf()
        if (index in current.indices) {
            current[index] = gain
            player.eqBands.value = current
        }
    }

    fun setBassBoost(value: Float) {
        player.bassBoost.value = value
    }

    fun setTrebleBoost(value: Float) {
        player.trebleBoost.value = value
    }

    fun applyPreset(name: String) {
        val preset = when (name.lowercase()) {
            "bass boost" -> floatArrayOf(8f, 5f, 0f, 0f, 0f)
            "acoustic" -> floatArrayOf(3f, 1f, 2f, 4f, 2f)
            "pop" -> floatArrayOf(-2f, -1f, 2f, 3f, -1f)
            "jazz" -> floatArrayOf(4f, 2f, -1f, 2f, 5f)
            "electronic" -> floatArrayOf(6f, 3f, 0f, 4f, 5f)
            else -> floatArrayOf(0f, 0f, 0f, 0f, 0f) // Flat
        }
        player.eqBands.value = preset
        player.bassBoost.value = if (name.lowercase() == "bass boost") 8f else 0f
        player.trebleBoost.value = if (name.lowercase() == "electronic" || name.lowercase() == "jazz") 5f else 0f
    }

    // --- Sleep Timer ---
    fun startSleepTimer(minutes: Int) = player.startSleepTimer(minutes)
    fun stopSleepTimer() = player.stopSleepTimer()

    // --- Search & Navigation Helpers ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _activeCategory.value = category
    }

    // --- Preferences Settings ---
    fun setThemeMode(mode: Int) = preferences.setThemeMode(mode)
    fun setAccentColor(hex: String) = preferences.setAccentColor(hex)
    fun setShakeToChange(enabled: Boolean) = preferences.setShakeToChange(enabled)
    fun setAudioQuality(quality: String) = preferences.setAudioQuality(quality)
    fun setCrossfade(enabled: Boolean) = preferences.setCrossfade(enabled)
    fun setGapless(enabled: Boolean) = preferences.setGapless(enabled)

    // --- Factory ---
    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = context.applicationContext as AuraApplication
                return AuraViewModel(app.repository, app.player, app.preferences) as T
            }
        }
    }
}
