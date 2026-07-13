package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val songId: Long,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val coverUri: String? = null
)

@Entity(tableName = "playlist_songs", primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Int,
    val songId: Long,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "play_history")
data class PlayHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val songId: Long,
    val playedAt: Long = System.currentTimeMillis(),
    val playCount: Int = 1
)

@Entity(tableName = "eq_presets")
data class EqPresetEntity(
    @PrimaryKey val name: String,
    val bass: Float = 0f,
    val treble: Float = 0f,
    val band60Hz: Float = 0f,
    val band230Hz: Float = 0f,
    val band910Hz: Float = 0f,
    val band4kHz: Float = 0f,
    val band14kHz: Float = 0f,
    val isCustom: Boolean = true
)
