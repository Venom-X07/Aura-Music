package com.example.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.*
import com.example.viewmodel.AuraViewModel

@Composable
fun NavigationContainer(
    viewModel: AuraViewModel
) {
    var showWelcomeFlow by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf("Home") }

    // Navigation state variables for detail pages
    var libraryInitialSection by remember { mutableStateOf("Dashboard") }

    val accentColor = MaterialTheme.colorScheme.primary

    if (showWelcomeFlow) {
        WelcomeScreen(
            viewModel = viewModel,
            onComplete = { showWelcomeFlow = false }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .testTag("app_navigation_bar")
                    ) {
                        val tabs = listOf(
                            NavigationTab("Home", Icons.Rounded.Home),
                            NavigationTab("Search", Icons.Rounded.Search),
                            NavigationTab("Library", Icons.Rounded.LibraryMusic),
                            NavigationTab("Settings", Icons.Rounded.Settings)
                        )

                        tabs.forEach { tab ->
                            val selected = currentTab == tab.title
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (tab.title == "Library") {
                                        libraryInitialSection = "Dashboard"
                                    }
                                    currentTab = tab.title
                                },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        tint = if (selected) accentColor else MaterialTheme.colorScheme.outline
                                    )
                                },
                                label = { Text(text = tab.title, color = if (selected) accentColor else MaterialTheme.colorScheme.outline) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = accentColor.copy(alpha = 0.12f)
                                ),
                                modifier = Modifier.testTag("nav_item_${tab.title.lowercase()}")
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                // Master dynamic view content switcher with animations
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    // Background artistic gradient glow
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.04f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.1f)
                                    )
                                )
                            )
                    )

                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        },
                        label = "TabContentTransition"
                    ) { tab ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (tab) {
                                "Home" -> HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToLibrarySection = { section ->
                                        libraryInitialSection = section
                                        currentTab = "Library"
                                    },
                                    onNavigateToPlaylist = { id ->
                                        libraryInitialSection = "playlist_details"
                                        currentTab = "Library"
                                    },
                                    onNavigateToAlbum = { title ->
                                        libraryInitialSection = "album_details"
                                        currentTab = "Library"
                                    },
                                    onNavigateToArtist = { name ->
                                        libraryInitialSection = "artist_details"
                                        currentTab = "Library"
                                    }
                                )

                                "Search" -> SearchScreen(
                                    viewModel = viewModel,
                                    onNavigateToAlbum = { title ->
                                        libraryInitialSection = "album_details"
                                        currentTab = "Library"
                                    },
                                    onNavigateToArtist = { name ->
                                        libraryInitialSection = "artist_details"
                                        currentTab = "Library"
                                    }
                                )

                                "Library" -> LibraryScreen(
                                    viewModel = viewModel,
                                    initialSection = libraryInitialSection
                                )

                                "Settings" -> SettingsScreen(
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }

            // Draw player overlay at the very top of everything!
            PlayerScreen(
                viewModel = viewModel,
                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
            )
        }
    }
}

data class NavigationTab(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
