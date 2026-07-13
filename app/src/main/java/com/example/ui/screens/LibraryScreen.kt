package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.database.PlaylistEntity
import com.example.data.model.Song
import com.example.viewmodel.AuraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: AuraViewModel,
    initialSection: String = "Dashboard"
) {
    val context = LocalContext.current
    var currentView by remember { mutableStateOf(if (initialSection == "Dashboard") "Dashboard" else initialSection) }
    var selectedItemDetail by remember { mutableStateOf("") } // Album title, artist name, folder path, or playlist ID

    val songs by viewModel.songs.collectAsState()
    val favorites by viewModel.favoriteIds.collectAsState()
    val playlists by viewModel.playlists.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()

    val accentColor = MaterialTheme.colorScheme.primary

    // Playlists dialog states
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }
    var showAddToPlaylistDialog by remember { mutableStateOf<Song?>(null) }

    // Intercept back presses in sub-views
    BackHandler(enabled = currentView != "Dashboard") {
        if (currentView.endsWith("_details")) {
            currentView = when {
                currentView.startsWith("album") -> "Albums"
                currentView.startsWith("artist") -> "Artists"
                currentView.startsWith("playlist") -> "Playlists"
                currentView.startsWith("folder") -> "Folders"
                else -> "Dashboard"
            }
        } else {
            currentView = "Dashboard"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentView) {
                            "Dashboard" -> "Library"
                            "Songs" -> "All Songs"
                            "Albums" -> "Albums"
                            "Artists" -> "Artists"
                            "Playlists" -> "Playlists"
                            "Folders" -> "Folders"
                            "Favorites" -> "Favorites"
                            "album_details" -> selectedItemDetail
                            "artist_details" -> selectedItemDetail
                            "playlist_details" -> "Playlist View"
                            "folder_details" -> "Folder View"
                            else -> "Library"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    if (currentView != "Dashboard") {
                        IconButton(onClick = {
                            if (currentView.endsWith("_details")) {
                                currentView = when {
                                    currentView.startsWith("album") -> "Albums"
                                    currentView.startsWith("artist") -> "Artists"
                                    currentView.startsWith("playlist") -> "Playlists"
                                    currentView.startsWith("folder") -> "Folders"
                                    else -> "Dashboard"
                                }
                            } else {
                                currentView = "Dashboard"
                            }
                        }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (currentView == "Playlists") {
                        IconButton(onClick = { showCreatePlaylistDialog = true }) {
                            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Create Playlist")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentView,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "LibraryViewNavigation"
            ) { view ->
                when (view) {
                    "Dashboard" -> LibraryDashboardView(
                        songsCount = songs.size,
                        playlistsCount = playlists.size,
                        favoritesCount = favorites.size,
                        foldersCount = songs.groupBy { it.folder }.keys.size,
                        accentColor = accentColor,
                        onNavigate = { currentView = it }
                    )

                    "Songs" -> SongsListView(
                        songs = songs,
                        favorites = favorites,
                        accentColor = accentColor,
                        onPlaySong = { song -> viewModel.playSong(song, songs) },
                        onToggleFav = { id -> viewModel.toggleFavorite(id) },
                        onAddToPlaylist = { showAddToPlaylistDialog = it }
                    )

                    "Albums" -> AlbumsGridView(
                        songs = songs,
                        onAlbumClick = {
                            selectedItemDetail = it
                            currentView = "album_details"
                        }
                    )

                    "album_details" -> AlbumDetailsView(
                        albumTitle = selectedItemDetail,
                        songs = songs.filter { it.album == selectedItemDetail },
                        favorites = favorites,
                        accentColor = accentColor,
                        onPlaySong = { song, queue -> viewModel.playSong(song, queue) },
                        onToggleFav = { id -> viewModel.toggleFavorite(id) }
                    )

                    "Artists" -> ArtistsListView(
                        songs = songs,
                        onArtistClick = {
                            selectedItemDetail = it
                            currentView = "artist_details"
                        }
                    )

                    "artist_details" -> ArtistDetailsView(
                        artistName = selectedItemDetail,
                        songs = songs.filter { it.artist == selectedItemDetail },
                        favorites = favorites,
                        accentColor = accentColor,
                        onPlaySong = { song, queue -> viewModel.playSong(song, queue) },
                        onToggleFav = { id -> viewModel.toggleFavorite(id) }
                    )

                    "Playlists" -> PlaylistsListView(
                        playlists = playlists,
                        onPlaylistClick = { id ->
                            selectedItemDetail = id.toString()
                            currentView = "playlist_details"
                        },
                        onDeletePlaylist = { id -> viewModel.deletePlaylist(id) }
                    )

                    "playlist_details" -> {
                        val playlistId = selectedItemDetail.toIntOrNull() ?: 0
                        val playlistSongsState = viewModel.getSongsInPlaylist(playlistId).collectAsState(initial = emptyList())
                        val playlistEntity = playlists.find { it.id == playlistId }
                        PlaylistDetailsView(
                            playlistName = playlistEntity?.name ?: "Custom Playlist",
                            songs = playlistSongsState.value,
                            favorites = favorites,
                            accentColor = accentColor,
                            onPlaySong = { song, queue -> viewModel.playSong(song, queue) },
                            onRemoveSong = { songId -> viewModel.removeSongFromPlaylist(playlistId, songId) },
                            onToggleFav = { id -> viewModel.toggleFavorite(id) }
                        )
                    }

                    "Folders" -> FoldersListView(
                        songs = songs,
                        onFolderClick = {
                            selectedItemDetail = it
                            currentView = "folder_details"
                        }
                    )

                    "folder_details" -> FolderDetailsView(
                        folderName = selectedItemDetail,
                        songs = songs.filter { it.folder == selectedItemDetail },
                        favorites = favorites,
                        accentColor = accentColor,
                        onPlaySong = { song, queue -> viewModel.playSong(song, queue) },
                        onToggleFav = { id -> viewModel.toggleFavorite(id) }
                    )

                    "Favorites" -> SongsListView(
                        songs = songs.filter { favorites.contains(it.id) },
                        favorites = favorites,
                        accentColor = accentColor,
                        onPlaySong = { song -> viewModel.playSong(song, songs.filter { favorites.contains(it.id) }) },
                        onToggleFav = { id -> viewModel.toggleFavorite(id) },
                        onAddToPlaylist = { showAddToPlaylistDialog = it }
                    )
                }
            }
        }

        // --- DIALOGS ---
        if (showCreatePlaylistDialog) {
            Dialog(onDismissRequest = { showCreatePlaylistDialog = false }) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "New Playlist",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = newPlaylistName,
                            onValueChange = { newPlaylistName = it },
                            placeholder = { Text("My Aura Beats") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accentColor),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TextButton(onClick = { showCreatePlaylistDialog = false }) {
                                Text("Cancel", color = MaterialTheme.colorScheme.outline)
                            }
                            Button(
                                onClick = {
                                    if (newPlaylistName.isNotBlank()) {
                                        viewModel.createPlaylist(newPlaylistName)
                                        newPlaylistName = ""
                                        showCreatePlaylistDialog = false
                                        Toast.makeText(context, "Playlist Created", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                            ) {
                                Text("Create", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Add to Playlist Selection Dialog
        if (showAddToPlaylistDialog != null) {
            val songToInsert = showAddToPlaylistDialog!!
            Dialog(onDismissRequest = { showAddToPlaylistDialog = null }) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Add to Playlist",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (playlists.isEmpty()) {
                            Text(
                                text = "Create a playlist first from the Playlists section.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 240.dp)) {
                                items(playlists) { playlist ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.addSongToPlaylist(playlist.id, songToInsert.id)
                                                showAddToPlaylistDialog = null
                                                Toast.makeText(context, "Added to ${playlist.name}", Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(vertical = 12.dp, horizontal = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Rounded.QueueMusic, contentDescription = null, tint = accentColor)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text = playlist.name, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { showAddToPlaylistDialog = null }) {
                            Text("Close", color = accentColor)
                        }
                    }
                }
            }
        }
    }
}

// Sub-view 1: Dashboard
@Composable
fun LibraryDashboardView(
    songsCount: Int,
    playlistsCount: Int,
    favoritesCount: Int,
    foldersCount: Int,
    accentColor: Color,
    onNavigate: (String) -> Unit
) {
    val menuItems = listOf(
        LibraryMenuItem("Songs", "Songs", "All tracks", SongsCountString(songsCount), Icons.Rounded.Audiotrack),
        LibraryMenuItem("Albums", "Albums", "Releases", "$songsCount songs", Icons.Rounded.Album),
        LibraryMenuItem("Artists", "Artists", "Performers", "By name", Icons.Rounded.Person),
        LibraryMenuItem("Playlists", "Playlists", "Mixes", "$playlistsCount playlists", Icons.Rounded.QueueMusic),
        LibraryMenuItem("Folders", "Folders", "File structures", "$foldersCount directories", Icons.Rounded.Folder),
        LibraryMenuItem("Favorites", "Favorites", "Heart beats", "$favoritesCount favorites", Icons.Rounded.Favorite)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(menuItems) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(item.destination) }
                    .testTag("library_item_${item.destination}"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = item.icon, contentDescription = null, tint = accentColor)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(text = item.subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = item.count, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

// Sub-view 2: Songs ListView
@Composable
fun SongsListView(
    songs: List<Song>,
    favorites: Set<Long>,
    accentColor: Color,
    onPlaySong: (Song) -> Unit,
    onToggleFav: (Long) -> Unit,
    onAddToPlaylist: (Song) -> Unit
) {
    if (songs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No songs found.", color = MaterialTheme.colorScheme.outline)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(songs) { song ->
            val isFav = favorites.contains(song.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaySong(song) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = song.albumArtUri,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onToggleFav(song.id) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFav) accentColor else MaterialTheme.colorScheme.outline
                        )
                    }

                    IconButton(onClick = { onAddToPlaylist(song) }) {
                        Icon(
                            imageVector = Icons.Rounded.PlaylistAdd,
                            contentDescription = "Add to playlist",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

// Sub-view 3: Albums GridView
@Composable
fun AlbumsGridView(
    songs: List<Song>,
    onAlbumClick: (String) -> Unit
) {
    val albums = remember(songs) {
        songs.groupBy { it.album }.map { (title, tracks) ->
            Triple(title, tracks.firstOrNull()?.artist ?: "Unknown Artist", tracks.firstOrNull()?.albumArtUri)
        }
    }

    if (albums.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No albums found.", color = MaterialTheme.colorScheme.outline)
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, bottom = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(albums) { (title, artist, artworkUrl) ->
            Column(modifier = Modifier.clickable { onAlbumClick(title) }) {
                AsyncImage(
                    model = artworkUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = artist, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// Sub-view 4: Album Details
@Composable
fun AlbumDetailsView(
    albumTitle: String,
    songs: List<Song>,
    favorites: Set<Long>,
    accentColor: Color,
    onPlaySong: (Song, List<Song>) -> Unit,
    onToggleFav: (Long) -> Unit
) {
    val firstSong = songs.firstOrNull()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 120.dp)
    ) {
        // Upper Album Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = firstSong?.albumArtUri,
                contentDescription = albumTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                error = painterResource(id = android.R.drawable.ic_menu_report_image)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = albumTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = firstSong?.artist ?: "Unknown Artist",
                style = MaterialTheme.typography.bodyLarge,
                color = accentColor,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Quick Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { if (songs.isNotEmpty()) onPlaySong(songs.first(), songs) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play", color = Color.White)
            }

            OutlinedButton(
                onClick = { if (songs.isNotEmpty()) onPlaySong(songs.shuffled().first(), songs) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, accentColor)
            ) {
                Icon(imageVector = Icons.Rounded.Shuffle, contentDescription = null, tint = accentColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Shuffle", color = accentColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tracks list
        songs.forEachIndexed { index, song ->
            val isFav = favorites.contains(song.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaySong(song, songs) }
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.width(28.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = song.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(text = FormatDuration(song.duration), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }

                IconButton(onClick = { onToggleFav(song.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFav) accentColor else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// Sub-view 5: Artists ListView
@Composable
fun ArtistsListView(
    songs: List<Song>,
    onArtistClick: (String) -> Unit
) {
    val artists = remember(songs) {
        songs.groupBy { it.artist }.map { (name, tracks) ->
            Pair(name, tracks.size)
        }
    }

    if (artists.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No artists found.", color = MaterialTheme.colorScheme.outline)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(artists) { (name, count) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onArtistClick(name) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(text = "$count tracks", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }

                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

// Sub-view 6: Artist Details
@Composable
fun ArtistDetailsView(
    artistName: String,
    songs: List<Song>,
    favorites: Set<Long>,
    accentColor: Color,
    onPlaySong: (Song, List<Song>) -> Unit,
    onToggleFav: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 120.dp)
    ) {
        // Large Circular Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.06f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Rounded.Person, contentDescription = null, tint = accentColor, modifier = Modifier.size(64.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = artistName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "${songs.size} Local Audio Tracks", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        }

        // Action controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { if (songs.isNotEmpty()) onPlaySong(songs.first(), songs) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play All", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Songs
        songs.forEach { song ->
            val isFav = favorites.contains(song.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaySong(song, songs) }
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = song.albumArtUri,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = song.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = song.album, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                IconButton(onClick = { onToggleFav(song.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFav) accentColor else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// Sub-view 7: Playlists
@Composable
fun PlaylistsListView(
    playlists: List<PlaylistEntity>,
    onPlaylistClick: (Int) -> Unit,
    onDeletePlaylist: (Int) -> Unit
) {
    if (playlists.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Rounded.QueueMusic, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(56.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "No Playlists", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap the plus button at the top right to create a new custom playlist.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(playlists) { playlist ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaylistClick(playlist.id) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Rounded.QueueMusic, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = playlist.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = { onDeletePlaylist(playlist.id) }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete Playlist", tint = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

// Sub-view 8: Playlist Details View
@Composable
fun PlaylistDetailsView(
    playlistName: String,
    songs: List<Song>,
    favorites: Set<Long>,
    accentColor: Color,
    onPlaySong: (Song, List<Song>) -> Unit,
    onRemoveSong: (Long) -> Unit,
    onToggleFav: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 120.dp)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(accentColor.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Rounded.QueueMusic, contentDescription = null, tint = accentColor, modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = playlistName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "${songs.size} songs", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }

        if (songs.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Playlist is empty.", color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(24.dp))
            }
            return
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { onPlaySong(songs.first(), songs) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play All", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        songs.forEach { song ->
            val isFav = favorites.contains(song.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaySong(song, songs) }
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = song.albumArtUri,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = song.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = song.artist, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onToggleFav(song.id) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFav) accentColor else MaterialTheme.colorScheme.outline
                        )
                    }

                    IconButton(onClick = { onRemoveSong(song.id) }) {
                        Icon(imageVector = Icons.Rounded.RemoveCircleOutline, contentDescription = "Remove", tint = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

// Sub-view 9: Folders ListView
@Composable
fun FoldersListView(
    songs: List<Song>,
    onFolderClick: (String) -> Unit
) {
    val folders = remember(songs) {
        songs.groupBy { it.folder }.map { (name, tracks) ->
            Pair(name, tracks.size)
        }
    }

    if (folders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No directories found.", color = MaterialTheme.colorScheme.outline)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(folders) { (name, count) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFolderClick(name) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Rounded.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(text = "$count tracks", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }

                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

// Sub-view 10: Folder Details
@Composable
fun FolderDetailsView(
    folderName: String,
    songs: List<Song>,
    favorites: Set<Long>,
    accentColor: Color,
    onPlaySong: (Song, List<Song>) -> Unit,
    onToggleFav: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 120.dp)
    ) {
        // Folder Header Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accentColor.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Rounded.Folder, contentDescription = null, tint = accentColor, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = folderName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Local Folder • ${songs.size} songs", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
            }
        }

        // Action
        Button(
            onClick = { if (songs.isNotEmpty()) onPlaySong(songs.first(), songs) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Play Folder Songs", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        songs.forEach { song ->
            val isFav = favorites.contains(song.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaySong(song, songs) }
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = song.albumArtUri,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = song.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = song.artist, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                IconButton(onClick = { onToggleFav(song.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFav) accentColor else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// Helpers
fun FormatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}

fun SongsCountString(count: Int): String {
    return if (count == 1) "1 song" else "$count songs"
}

data class LibraryMenuItem(
    val destination: String,
    val title: String,
    val subtitle: String,
    val count: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// Simple custom BackHandler helper to avoid AndroidX lifecycle imports
@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
}
