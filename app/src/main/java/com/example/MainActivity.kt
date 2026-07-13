package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.NavigationContainer
import com.example.ui.theme.AuraTheme
import com.example.viewmodel.AuraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Render beautiful immersive status and navigation bars (Edge-to-Edge)
        enableEdgeToEdge()
        
        setContent {
            // Retrieve view model with our companion factory fetching repository & ExoPlayer
            val viewModel: AuraViewModel = viewModel(factory = AuraViewModel.provideFactory(applicationContext))
            
            // Dynamic appearance observations
            val themeMode by viewModel.themeMode.collectAsState()
            val accentColorHex by viewModel.accentColorHex.collectAsState()

            AuraTheme(
                themeMode = themeMode,
                accentColorHex = accentColorHex
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavigationContainer(viewModel = viewModel)
                }
            }
        }
    }
}
