package com.example.data.preferences

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuraPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("aura_music_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(prefs.getInt(KEY_THEME_MODE, 1)) // Default: Dark Mode First (1)
    val themeMode: StateFlow<Int> = _themeMode

    private val _accentColorHex = MutableStateFlow(prefs.getString(KEY_ACCENT_COLOR, "#FF3B30") ?: "#FF3B30") // Default Apple Red
    val accentColorHex: StateFlow<String> = _accentColorHex

    private val _shakeToChange = MutableStateFlow(prefs.getBoolean(KEY_SHAKE, false))
    val shakeToChange: StateFlow<Boolean> = _shakeToChange

    private val _audioQuality = MutableStateFlow(prefs.getString(KEY_QUALITY, "HQ") ?: "HQ")
    val audioQuality: StateFlow<String> = _audioQuality

    private val _crossfade = MutableStateFlow(prefs.getBoolean(KEY_CROSSFADE, false))
    val crossfade: StateFlow<Boolean> = _crossfade

    private val _gapless = MutableStateFlow(prefs.getBoolean(KEY_GAPLESS, true))
    val gapless: StateFlow<Boolean> = _gapless

    fun setThemeMode(mode: Int) {
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply()
        _themeMode.value = mode
    }

    fun setAccentColor(hex: String) {
        prefs.edit().putString(KEY_ACCENT_COLOR, hex).apply()
        _accentColorHex.value = hex
    }

    fun setShakeToChange(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHAKE, enabled).apply()
        _shakeToChange.value = enabled
    }

    fun setAudioQuality(quality: String) {
        prefs.edit().putString(KEY_QUALITY, quality).apply()
        _audioQuality.value = quality
    }

    fun setCrossfade(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CROSSFADE, enabled).apply()
        _crossfade.value = enabled
    }

    fun setGapless(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_GAPLESS, enabled).apply()
        _gapless.value = enabled
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_ACCENT_COLOR = "accent_color"
        private const val KEY_SHAKE = "shake_to_change"
        private const val KEY_QUALITY = "audio_quality"
        private const val KEY_CROSSFADE = "crossfade"
        private const val KEY_GAPLESS = "gapless_playback"
    }
}
