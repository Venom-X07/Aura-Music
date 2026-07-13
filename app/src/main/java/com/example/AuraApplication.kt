package com.example

import android.app.Application
import com.example.data.database.AuraDatabase
import com.example.data.preferences.AuraPreferences
import com.example.data.repository.MusicRepository
import com.example.player.AuraAudioPlayer

class AuraApplication : Application() {

    lateinit var database: AuraDatabase
        private set

    lateinit var repository: MusicRepository
        private set

    lateinit var player: AuraAudioPlayer
        private set

    lateinit var preferences: AuraPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        
        database = AuraDatabase.getDatabase(this)
        repository = MusicRepository(this, database.auraDao())
        player = AuraAudioPlayer(this)
        preferences = AuraPreferences(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        player.release()
    }
}
