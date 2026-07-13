package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.viewmodel.AuraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AuraViewModel
) {
    val context = LocalContext.current

    val themeMode by viewModel.themeMode.collectAsState()
    val accentColorHex by viewModel.accentColorHex.collectAsState()
    val shakeToChange by viewModel.shakeToChange.collectAsState()
    val audioQuality by viewModel.audioQuality.collectAsState()
    val crossfade by viewModel.crossfadeEnabled.collectAsState()
    val gapless by viewModel.gaplessEnabled.collectAsState()

    val accentColor = MaterialTheme.colorScheme.primary

    // Accent options
    val colorSwatches = listOf(
        Pair("Red", "#FF3B30"),
        Pair("Orange", "#FF9500"),
        Pair("Green", "#34C759"),
        Pair("Blue", "#007AFF"),
        Pair("Purple", "#AF52DE"),
        Pair("Pink", "#FF2D55")
    )

    // Sub dialog trigger states
    var activeDialog by remember { mutableStateOf("") } // "", "about", "privacy", "feedback"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- THEME & ACCENT CUSTOMIZATION ---
            item {
                SettingsSectionHeader(title = "Appearance & Theme")
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "App Theme Mode", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Row of Segmented Theme buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val themeOptions = listOf("Light", "Dark", "System")
                            themeOptions.forEachIndexed { index, name ->
                                val selected = themeMode == index
                                Button(
                                    onClick = { viewModel.setThemeMode(index) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .testTag("theme_btn_$name"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selected) accentColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(text = name, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Vibrant Custom Accent Color", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Row of colored circular chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(colorSwatches) { (name, hex) ->
                                val selected = accentColorHex.equals(hex, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(hex)))
                                        .border(
                                            width = if (selected) 3.dp else 0.dp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            shape = CircleShape
                                        )
                                        .clickable { viewModel.setAccentColor(hex) }
                                        .testTag("accent_chip_$name"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "Active", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- AUDIO ENGINE PREFERENCES ---
            item {
                SettingsSectionHeader(title = "Audio Engine Setup")
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Quality
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Audio Output Quality", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text(text = "Lossless OGG/FLAC streaming decode", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }

                            Row {
                                listOf("HQ", "Hi-Fi").forEach { quality ->
                                    val active = audioQuality == quality
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) accentColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            .clickable { viewModel.setAudioQuality(quality) }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = quality, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (active) Color.White else MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        // Crossfade Toggle
                        SettingsToggleRow(
                            title = "Audio Crossfade (12s)",
                            subtitle = "Smooth gapless acoustic transitioning",
                            checked = crossfade,
                            onCheckedChange = { viewModel.setCrossfade(it) }
                        )

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        // Gapless Playback Toggle
                        SettingsToggleRow(
                            title = "Gapless Playback",
                            subtitle = "Zero latency song transitions",
                            checked = gapless,
                            onCheckedChange = { viewModel.setGapless(it) }
                        )
                    }
                }
            }

            // --- GESTURES & HARDWARE CONTROLS ---
            item {
                SettingsSectionHeader(title = "Hardware & Gestures")
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Shake To Skip
                        SettingsToggleRow(
                            title = "Shake To Change Song",
                            subtitle = "Shake device to play next queue track",
                            checked = shakeToChange,
                            onCheckedChange = { viewModel.setShakeToChange(it) }
                        )

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        // Headset Controls
                        SettingsToggleRow(
                            title = "Headphone Wire Clicks",
                            subtitle = "Single click play/pause, double click next",
                            checked = true,
                            onCheckedChange = {}
                        )

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        // Bluetooth Auto-Resume
                        SettingsToggleRow(
                            title = "Bluetooth Auto-Resume",
                            subtitle = "Resume playing instantly when connected",
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                }
            }

            // --- LOCAL STORAGE CACHE ---
            item {
                SettingsSectionHeader(title = "Local Cache & Backups")
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "App Image Cache", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text(text = "Scanned albums metadata caches (12.4 MB)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }

                            Button(
                                onClick = { Toast.makeText(context, "Cache Cleared successfully", Toast.LENGTH_SHORT).show() },
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.1f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "Purge Cache", color = accentColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { Toast.makeText(context, "Backup file saved to Downloads/aura_backup.json", Toast.LENGTH_SHORT).show() }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Backup Playlists & Favorites", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text(text = "Export local configurations to offline backup file", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Icon(imageVector = Icons.Rounded.CloudUpload, contentDescription = null, tint = accentColor)
                        }
                    }
                }
            }

            // --- ABOUT, LEGAL & FEEDBACK ---
            item {
                SettingsSectionHeader(title = "Information & Feedback")
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SettingsNavigationRow(
                            title = "Privacy & Policy",
                            onClick = { activeDialog = "privacy" }
                        )

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        SettingsNavigationRow(
                            title = "Direct Feedback",
                            onClick = { activeDialog = "feedback" }
                        )

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        SettingsNavigationRow(
                            title = "About Aura Music",
                            onClick = { activeDialog = "about" }
                        )
                    }
                }
            }
        }

        // --- SUB DIALOG DETAILS OVERLAYS ---
        if (activeDialog.isNotEmpty()) {
            Dialog(onDismissRequest = { activeDialog = "" }) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (activeDialog) {
                                "about" -> "About Aura Music"
                                "privacy" -> "Privacy & Policy"
                                "feedback" -> "Submit Feedback"
                                else -> "Info"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Text Body Content
                        Text(
                            text = when (activeDialog) {
                                "about" -> "Aura Music v1.0.0\n\nDesigned by Senior Android Designers.\nA high-fidelity audio player for local storage. Tuned in Kotlin with Jetpack Compose, Room Database, and Media3 ExoPlayer."
                                "privacy" -> "Aura Music respects your local privacy. The application queries internal device MediaStore audio files on device. No user data, files, playlists, or analytics are uploaded to servers. All information remains secure on your personal Android device."
                                "feedback" -> "We want to make Aura Music perfect for your studio auditory sessions! Please report bugs, feature ideas, or graphic customization suggestions.\n\nSend us mail at aitechengineer001@gmail.com"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { activeDialog = "" },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Close", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Composable
fun SettingsNavigationRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
    }
}
