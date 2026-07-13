package com.example.data.model

import java.io.Serializable

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val path: String,
    val albumArtUri: String? = null,
    val trackNumber: Int = 0,
    val year: Int = 0,
    val genre: String = "Unknown",
    val folder: String = "Download",
    val composer: String? = null,
    val size: Long = 0,
    val dateAdded: Long = 0,
    val isFavorite: Boolean = false
) : Serializable

data class Artist(
    val name: String,
    val songCount: Int,
    val albumCount: Int,
    val playTimeMs: Long = 0,
    val isFavorite: Boolean = false
) : Serializable

data class Album(
    val title: String,
    val artist: String,
    val songCount: Int,
    val year: Int = 0,
    val isFavorite: Boolean = false,
    val albumArtUri: String? = null
) : Serializable

data class Folder(
    val name: String,
    val path: String,
    val songCount: Int
) : Serializable

data class LyricsLine(
    val timeMs: Long,
    val text: String
) : Serializable

data class PlaybackStats(
    val totalPlayTimeMs: Long,
    val favoriteArtist: String,
    val favoriteSong: String,
    val totalSongsPlayed: Int,
    val achievements: List<Achievement>
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val iconName: String
)
