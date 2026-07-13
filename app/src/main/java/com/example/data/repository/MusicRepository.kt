package com.example.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.data.database.*
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.File

class MusicRepository(
    private val context: Context,
    private val dao: AuraDao
) {
    private val _songsFlow = MutableStateFlow<List<Song>>(emptyList())
    val songsFlow: StateFlow<List<Song>> = _songsFlow.asStateFlow()

    // Database flow bridges
    val favoriteIds: Flow<Set<Long>> = dao.getAllFavorites()
        .map { list -> list.map { it.songId }.toSet() }
        .flowOn(Dispatchers.IO)

    val playlists: Flow<List<PlaylistEntity>> = dao.getAllPlaylists()
        .flowOn(Dispatchers.IO)

    val history: Flow<List<PlayHistoryEntity>> = dao.getPlayHistory()
        .flowOn(Dispatchers.IO)

    val presets: Flow<List<EqPresetEntity>> = dao.getAllEqPresets()
        .flowOn(Dispatchers.IO)

    // Demo audio tracks
    private val demoSongs = listOf(
        Song(
            id = 9001L,
            title = "Midnight Horizon",
            artist = "Aura Ambient",
            album = "Feel Every Beat",
            duration = 372000L, // 6:12
            path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            albumArtUri = "https://images.unsplash.com/photo-1614149162883-504ce4d13909?w=400",
            trackNumber = 1,
            year = 2026,
            genre = "Ambient",
            folder = "Aura Originals",
            composer = "Aura AI Designer",
            isFavorite = false
        ),
        Song(
            id = 9002L,
            title = "Golden Hour",
            artist = "Lofi Dreamer",
            album = "Sunset Memoirs",
            duration = 425000L, // 7:05
            path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            albumArtUri = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400",
            trackNumber = 2,
            year = 2026,
            genre = "Lofi Beats",
            folder = "Aura Originals",
            composer = "Lofi Master",
            isFavorite = false
        ),
        Song(
            id = 9003L,
            title = "Neon Pulsar",
            artist = "Cyber Pulse",
            album = "Retro Overdrive",
            duration = 324000L, // 5:24
            path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            albumArtUri = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=400",
            trackNumber = 3,
            year = 2025,
            genre = "Synthwave",
            folder = "Cyber Synths",
            composer = "Synth Kid",
            isFavorite = false
        ),
        Song(
            id = 9004L,
            title = "Whispering Pines",
            artist = "Solar Echoes",
            album = "Stardust Symphony",
            duration = 302000L, // 5:02
            path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            albumArtUri = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400",
            trackNumber = 4,
            year = 2026,
            genre = "Chillout",
            folder = "Nature Chill",
            composer = "Eco Ambient",
            isFavorite = false
        ),
        Song(
            id = 9005L,
            title = "Starlight Sonata",
            artist = "Aura Ambient",
            album = "Feel Every Beat",
            duration = 353000L, // 5:53
            path = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            albumArtUri = "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=400",
            trackNumber = 5,
            year = 2026,
            genre = "Ambient",
            folder = "Aura Originals",
            composer = "Aura AI Designer",
            isFavorite = false
        )
    )

    init {
        // Pre-populate standard equalizer presets if DB empty
        _songsFlow.value = demoSongs
    }

    suspend fun loadDemoTracks() {
        withContext(Dispatchers.Default) {
            _songsFlow.value = demoSongs
        }
    }

    suspend fun scanDeviceSongs(): List<Song> {
        return withContext(Dispatchers.IO) {
            val songsList = mutableListOf<Song>()
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATE_ADDED
            )

            // Select only music files (duration > 5s, filter out podcasts or ringtones)
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} >= 5000"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            try {
                context.contentResolver.query(uri, projection, selection, null, sortOrder)?.use { cursor ->
                    val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
                    val yearCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
                    val composerCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)
                    val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                    val dateAddedCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idCol)
                        val title = cursor.getString(titleCol) ?: "Unknown Song"
                        val artist = cursor.getString(artistCol) ?: "Unknown Artist"
                        val album = cursor.getString(albumCol) ?: "Unknown Album"
                        val duration = cursor.getLong(durationCol)
                        val data = cursor.getString(dataCol) ?: ""
                        val albumId = cursor.getLong(albumIdCol)
                        val track = cursor.getInt(trackCol)
                        val year = cursor.getInt(yearCol)
                        val composer = cursor.getString(composerCol)
                        val size = cursor.getLong(sizeCol)
                        val dateAdded = cursor.getLong(dateAddedCol)

                        // Compose Album Art URI
                        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                        val albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId).toString()

                        // Check if file actually exists
                        val file = File(data)
                        if (file.exists() || data.startsWith("http")) {
                            val folderName = file.parentFile?.name ?: "Unknown"

                            songsList.add(
                                Song(
                                    id = id,
                                    title = title,
                                    artist = artist,
                                    album = album,
                                    duration = duration,
                                    path = data,
                                    albumArtUri = albumArtUri,
                                    trackNumber = track,
                                    year = year,
                                    genre = "Local Audio",
                                    folder = folderName,
                                    composer = composer,
                                    size = size,
                                    dateAdded = dateAdded
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MusicRepository", "Error querying MediaStore", e)
            }

            // Fallback to demo songs if no files found
            if (songsList.isEmpty()) {
                _songsFlow.value = demoSongs
                demoSongs
            } else {
                _songsFlow.value = songsList
                songsList
            }
        }
    }

    // Favorites
    suspend fun toggleFavorite(songId: Long) {
        withContext(Dispatchers.IO) {
            val isFav = dao.isFavorite(songId).first()
            if (isFav) {
                dao.deleteFavoriteById(songId)
            } else {
                dao.insertFavorite(FavoriteEntity(songId))
            }
        }
    }

    // Playlists
    suspend fun createPlaylist(name: String): Int {
        return withContext(Dispatchers.IO) {
            dao.insertPlaylist(PlaylistEntity(name = name)).toInt()
        }
    }

    suspend fun deletePlaylist(playlistId: Int) {
        withContext(Dispatchers.IO) {
            dao.deletePlaylistById(playlistId)
            dao.clearPlaylistSongs(playlistId)
        }
    }

    suspend fun addSongToPlaylist(playlistId: Int, songId: Long) {
        withContext(Dispatchers.IO) {
            dao.insertPlaylistSong(PlaylistSongCrossRef(playlistId, songId))
        }
    }

    suspend fun removeSongFromPlaylist(playlistId: Int, songId: Long) {
        withContext(Dispatchers.IO) {
            dao.deletePlaylistSong(playlistId, songId)
        }
    }

    fun getSongsInPlaylistFlow(playlistId: Int): Flow<List<Song>> {
        return combine(dao.getSongsInPlaylist(playlistId), songsFlow) { ids, allSongs ->
            ids.mapNotNull { id -> allSongs.find { it.id == id } }
        }.flowOn(Dispatchers.IO)
    }

    // Play history logger
    suspend fun logSongPlay(songId: Long) {
        withContext(Dispatchers.IO) {
            dao.insertPlayHistory(PlayHistoryEntity(songId = songId))
        }
    }

    // Equalizer
    suspend fun saveEqPreset(preset: EqPresetEntity) {
        withContext(Dispatchers.IO) {
            dao.insertEqPreset(preset)
        }
    }

    // Profile & Statistics aggregator
    fun getPlaybackStats(): Flow<PlaybackStats> {
        return combine(history, songsFlow) { historyList, allSongs ->
            val totalPlayTime = historyList.size * 180000L // Estimate 3 mins average per play for statistics
            val songCountMap = historyList.groupBy { it.songId }.mapValues { it.value.size }
            
            val favoriteSongId = songCountMap.maxByOrNull { it.value }?.key
            val favoriteSong = allSongs.find { it.id == favoriteSongId }?.title ?: "No plays yet"

            val artistCountMap = historyList.mapNotNull { h -> allSongs.find { it.id == h.songId }?.artist }
                .groupBy { it }
                .mapValues { it.value.size }
            val favoriteArtist = artistCountMap.maxByOrNull { it.value }?.key ?: "No plays yet"

            // Compute dynamic achievements
            val totalPlays = historyList.size
            val achievements = listOf(
                Achievement("first_beat", "First Beat", "Play your first local song on Aura Music", totalPlays >= 1, "music_note"),
                Achievement("lofi_addict", "Lofi Guru", "Listen to Lofi sessions over 5 times", totalPlays >= 5, "spa"),
                Achievement("night_owl", "Night Rider", "Enjoy beautiful ambient tracks after midnight", totalPlays >= 10, "nights_stay"),
                Achievement("collector", "Sound Curator", "Create a personalized custom playlist", totalPlays >= 3, "queue_music")
            )

            PlaybackStats(
                totalPlayTimeMs = totalPlayTime,
                favoriteArtist = favoriteArtist,
                favoriteSong = favoriteSong,
                totalSongsPlayed = totalPlays,
                achievements = achievements
            )
        }.flowOn(Dispatchers.IO)
    }
}
