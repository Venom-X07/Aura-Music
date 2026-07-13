package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Song
import com.example.viewmodel.AuraViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val position by viewModel.playbackPosition.collectAsState()
    val queue by viewModel.queue.collectAsState()
    val currentSongIndex by viewModel.currentSongIndex.collectAsState()
    val favorites by viewModel.favoriteIds.collectAsState()
    val speed by viewModel.playbackSpeed.collectAsState()
    val shuffle by viewModel.shuffleEnabled.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val timerSeconds by viewModel.sleepTimerSeconds.collectAsState()

    val accentColor = MaterialTheme.colorScheme.primary
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Screen State
    var isExpanded by remember { mutableStateOf(false) }
    var activeSheet by remember { mutableStateOf("") } // "", "lyrics", "equalizer", "timer", "queue"

    // Rotation state of album vinyl
    var rotationAngle by remember { mutableStateOf(0f) }
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                rotationAngle = (rotationAngle + 1f) % 360f
                delay(16) // ~60fps
            }
        }
    }

    if (currentSong == null) return

    val song = currentSong!!
    val isFav = favorites.contains(song.id)

    Box(
        modifier = if (isExpanded) {
            modifier.fillMaxSize()
        } else {
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
        }
    ) {
        // --- 1. PERSISTENT MINI PLAYER ---
        if (!isExpanded) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 88.dp) // Sits above bottom navigation bar
                    .fillMaxWidth()
                    .height(68.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.88f))
                    .border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .clickable { isExpanded = true }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            if (dragAmount.y < -10) {
                                isExpanded = true
                            }
                        }
                    }
                    .testTag("mini_player")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = song.albumArtUri,
                        contentDescription = song.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        error = painterResource(id = android.R.drawable.ic_menu_report_image)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.artist,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Mini Player Controls
                    IconButton(onClick = { viewModel.toggleFavorite(song.id) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFav) accentColor else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.togglePlayPause() },
                        modifier = Modifier.testTag("mini_play_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.skipToNext() }) {
                        Icon(
                            imageVector = Icons.Rounded.SkipNext,
                            contentDescription = "Next",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.stopPlayback() },
                        modifier = Modifier.testTag("mini_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close player",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Small linear seek progress bar at bottom of mini player
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                        .align(Alignment.BottomCenter)
                ) {
                    val progress = if (song.duration > 0) position.toFloat() / song.duration else 0f
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(accentColor)
                    )
                }
            }
        }

        // --- 2. EXPANDED FULL SCREEN PLAYER ---
        AnimatedVisibility(
            visible = isExpanded,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            if (dragAmount.y > 15) {
                                isExpanded = false
                            }
                        }
                    }
                    .testTag("expanded_player")
            ) {
                // Blur background overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.15f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .systemBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isExpanded = false }) {
                            Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "Collapse", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(32.dp))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "PLAYING FROM",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = song.folder,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Listening to ${song.title} by ${song.artist} on Aura Music!")
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Track"))
                            }) {
                                Icon(imageVector = Icons.Rounded.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onBackground)
                            }

                            IconButton(onClick = {
                                viewModel.stopPlayback()
                                isExpanded = false
                            }) {
                                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Stop playback", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(28.dp))
                            }
                        }
                    }

                    // Vinyl Rotating Album Cover
                    Box(
                        modifier = Modifier
                            .size(280.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .rotate(rotationAngle),
                        contentAlignment = Alignment.Center
                    ) {
                        // Vinyl Groove Lines
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.White.copy(alpha = 0.05f),
                                            Color.Black
                                        )
                                    )
                                )
                        )

                        AsyncImage(
                            model = song.albumArtUri,
                            contentDescription = song.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(170.dp)
                                .clip(CircleShape)
                                .border(4.dp, Color.Black, CircleShape),
                            error = painterResource(id = android.R.drawable.ic_menu_report_image)
                        )

                        // Spindle Hole
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background)
                        )
                    }

                    // Metadata Details
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = song.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${song.artist} • ${song.album}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.outline,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row {
                                IconButton(onClick = { viewModel.toggleFavorite(song.id) }) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (isFav) accentColor else MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(accentColor.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Rounded.Check, contentDescription = "Local file", tint = accentColor, modifier = Modifier.size(12.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Dynamic Visualizer Bars
                        MusicVisualizerRow(isPlaying = isPlaying, accentColor = accentColor)
                    }

                    // Slider and Timestamp seek indicators
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Slider(
                            value = if (song.duration > 0) position.toFloat() else 0f,
                            onValueChange = { viewModel.seekTo(it.toLong()) },
                            valueRange = 0f..(song.duration.toFloat().coerceAtLeast(1f)),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = accentColor,
                                inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("player_seek_slider")
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = FormatDuration(position), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(text = "-" + FormatDuration((song.duration - position).coerceAtLeast(0)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    // Player Main Audio Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.toggleShuffle() }) {
                            Icon(
                                imageVector = Icons.Rounded.Shuffle,
                                contentDescription = "Shuffle",
                                tint = if (shuffle) accentColor else MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(onClick = { viewModel.skipToPrevious() }) {
                            Icon(imageVector = Icons.Rounded.SkipPrevious, contentDescription = "Prev", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(44.dp))
                        }

                        // Big Play / Pause Circular Bubble
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(accentColor)
                                .clickable { viewModel.togglePlayPause() }
                                .testTag("expanded_play_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        IconButton(onClick = { viewModel.skipToNext() }) {
                            Icon(imageVector = Icons.Rounded.SkipNext, contentDescription = "Next", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(44.dp))
                        }

                        IconButton(onClick = { viewModel.cycleRepeatMode() }) {
                            Icon(
                                imageVector = when (repeatMode) {
                                    1 -> Icons.Rounded.RepeatOne
                                    2 -> Icons.Rounded.Repeat
                                    else -> Icons.Rounded.RepeatOn
                                },
                                contentDescription = "Repeat",
                                tint = if (repeatMode > 0) accentColor else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Feature Sheet Tray buttons (Lyrics, EQ, Timer, Queue)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { activeSheet = "lyrics" }) {
                            Icon(imageVector = Icons.Rounded.ChatBubbleOutline, contentDescription = "Lyrics", tint = MaterialTheme.colorScheme.onBackground)
                        }

                        IconButton(onClick = { activeSheet = "equalizer" }) {
                            Icon(imageVector = Icons.Rounded.GraphicEq, contentDescription = "Equalizer", tint = MaterialTheme.colorScheme.onBackground)
                        }

                        IconButton(onClick = { activeSheet = "timer" }) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Rounded.Timer, contentDescription = "Sleep Timer", tint = if (timerSeconds > 0) accentColor else MaterialTheme.colorScheme.onBackground)
                                if (timerSeconds > 0) {
                                    Text(
                                        text = "${timerSeconds / 60}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }
                            }
                        }

                        IconButton(onClick = { activeSheet = "queue" }) {
                            Icon(imageVector = Icons.Rounded.QueueMusic, contentDescription = "Queue", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }
        }

        // --- 3. BOTTOM SHEETS OVERLAYS ---
        AnimatedVisibility(
            visible = activeSheet.isNotEmpty(),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { activeSheet = "" },
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .clickable(enabled = false) {}, // prevent click-through
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        // Drag Indicator Handle
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(40.dp)
                                .height(5.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                                .clickable { activeSheet = "" }
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Dynamic Sheet Router
                        when (activeSheet) {
                            "lyrics" -> LyricsPanel(song = song, accentColor = accentColor)
                            "equalizer" -> EqualizerPanel(viewModel = viewModel, accentColor = accentColor)
                            "timer" -> SleepTimerPanel(viewModel = viewModel, activeSeconds = timerSeconds, accentColor = accentColor, onDismiss = { activeSheet = "" })
                            "queue" -> QueuePanel(
                                queueList = queue,
                                currentIndex = currentSongIndex,
                                accentColor = accentColor,
                                onRemove = { id -> viewModel.removeFromQueue(id) },
                                onClear = { viewModel.clearQueue(); activeSheet = "" },
                                onPlayIndex = { index ->
                                    if (index in queue.indices) {
                                        viewModel.playSong(queue[index], queue)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Sub-Component 1: Beautiful Auto-Scrolling Lyrics Panel
@Composable
fun LyricsPanel(song: Song, accentColor: Color) {
    val lyricsLines = remember(song) {
        listOf(
            "Welcome to Aura Music",
            "Feel every beat pounding inside",
            "This is your premium offline sanctuary",
            "Where the studio frequencies reside...",
            "Tuning out the chaos of the day",
            "We breathe in the frequency",
            "We sail away on the sonic wave",
            "Starlight lullabies soothing the soul",
            "Rest your mind...",
            "Feel every beat."
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Lyrics • ${song.title}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(lyricsLines) { index, line ->
                val highlight = index == 3 // fake active line highlight for gorgeous visual polish
                Text(
                    text = line,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
                    color = if (highlight) accentColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = if (highlight) 20.sp else 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// Sub-Component 2: Custom Equalizer Sliders Panel
@Composable
fun EqualizerPanel(viewModel: AuraViewModel, accentColor: Color) {
    val bands = viewModel.customEqBands.collectAsState()
    val bass = viewModel.bassBoost.collectAsState()
    val treble = viewModel.trebleBoost.collectAsState()

    val bandLabels = listOf("60Hz", "230Hz", "910Hz", "4kHz", "14kHz")
    val presets = listOf("Flat", "Bass Boost", "Acoustic", "Pop", "Jazz", "Electronic")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Aura Studio Equalizer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preset Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEach { preset ->
                FilterChip(
                    selected = false, // simple static select triggers
                    onClick = { viewModel.applyPreset(preset) },
                    label = { Text(preset) },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bass Boost Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Bass Boost", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(90.dp))
            Slider(
                value = bass.value,
                onValueChange = { viewModel.setBassBoost(it) },
                valueRange = 0f..15f,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(activeTrackColor = accentColor)
            )
            Text(text = "${bass.value.toInt()}dB", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(40.dp), textAlign = TextAlign.End)
        }

        // Treble Boost Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Treble Boost", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(90.dp))
            Slider(
                value = treble.value,
                onValueChange = { viewModel.setTrebleBoost(it) },
                valueRange = 0f..15f,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(activeTrackColor = accentColor)
            )
            Text(text = "${treble.value.toInt()}dB", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(40.dp), textAlign = TextAlign.End)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Manual 5-Band Tuning", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        // 5 manual bands sliders
        bands.value.forEachIndexed { index, gain ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = bandLabels[index], style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(60.dp))
                Slider(
                    value = gain,
                    onValueChange = { viewModel.setEqBand(index, it) },
                    valueRange = -10f..10f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(activeTrackColor = accentColor)
                )
                Text(
                    text = String.format("%+d", gain.toInt()),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

// Sub-Component 3: Sleep Timer Selection Panel
@Composable
fun SleepTimerPanel(
    viewModel: AuraViewModel,
    activeSeconds: Int,
    accentColor: Color,
    onDismiss: () -> Unit
) {
    val options = listOf(
        Pair("Off", 0),
        Pair("5 Minutes", 5),
        Pair("15 Minutes", 15),
        Pair("30 Minutes", 30),
        Pair("45 Minutes", 45),
        Pair("60 Minutes", 60)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Sleep Timer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (activeSeconds > 0) {
            Card(
                colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Active Countdown",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = String.format("%02d:%02d remaining", activeSeconds / 60, activeSeconds % 60),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        options.forEach { (label, minutes) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (minutes == 0) {
                            viewModel.stopSleepTimer()
                        } else {
                            viewModel.startSleepTimer(minutes)
                        }
                        onDismiss()
                    }
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, style = MaterialTheme.typography.bodyLarge)
                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

// Sub-Component 4: Active Queue Panel
@Composable
fun QueuePanel(
    queueList: List<Song>,
    currentIndex: Int,
    accentColor: Color,
    onRemove: (Long) -> Unit,
    onClear: () -> Unit,
    onPlayIndex: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Playing Next",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            TextButton(onClick = onClear) {
                Text("Clear All", color = accentColor)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (queueList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Queue is empty.", color = MaterialTheme.colorScheme.outline)
            }
            return
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(queueList) { index, song ->
                val playing = index == currentIndex
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (playing) accentColor.copy(alpha = 0.06f) else Color.Transparent)
                        .clickable { onPlayIndex(index) }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (playing) Icons.Rounded.VolumeUp else Icons.Rounded.DragHandle,
                        contentDescription = null,
                        tint = if (playing) accentColor else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (playing) FontWeight.Bold else FontWeight.Normal,
                            color = if (playing) accentColor else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.artist,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(onClick = { onRemove(song.id) }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

// Sub-Component 5: Animated Music Visualizer Row of 10 Bars
@Composable
fun MusicVisualizerRow(isPlaying: Boolean, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        val barsCount = 12
        for (i in 0 until barsCount) {
            val infiniteTransition = rememberInfiniteTransition(label = "visualizer_bar_$i")
            val animatedHeight by if (isPlaying) {
                infiniteTransition.animateFloat(
                    initialValue = 0.15f,
                    targetValue = 0.95f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 350 + (i * 45), // staggered
                            easing = FastOutLinearInEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "barHeight"
                )
            } else {
                remember { mutableStateOf(0.12f) }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .width(4.dp)
                    .fillMaxHeight(animatedHeight)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                accentColor,
                                accentColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }
    }
}
