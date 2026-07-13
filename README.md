<div align="center">

# 🎵 AURA MUSIC

### **Next-Generation Music Player for Android**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-14+-green?style=flat-square&logo=android)](https://developer.android.com)
[![Gradle](https://img.shields.io/badge/Gradle-8.0-darkblue?style=flat-square&logo=gradle)](https://gradle.org)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

*Experience Music Like Never Before - Seamless, Beautiful, Powerful*

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [System Requirements](#-system-requirements)
- [Architecture & Technology Stack](#-architecture--technology-stack)
- [Installation Guide](#-installation--setup-guide)
- [Build & Compilation](#-build--compilation)
- [Configuration](#-configuration)
- [Features in Detail](#-comprehensive-features-guide)
- [Usage Guide](#-complete-usage-guide)
- [Project Structure](#-project-structure)
- [Development](#-development)
- [API Integration](#-api-integration)
- [Performance Optimization](#-performance-optimization)
- [Security Features](#-security-features)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

---

## 🎯 Overview

**Aura Music** is a state-of-the-art music player application designed specifically for Android devices. Built entirely in **Kotlin**, Aura Music combines cutting-edge technology with an intuitive user interface to deliver an exceptional audio experience. Whether you're a casual listener or an audiophile, Aura Music provides powerful features, seamless playback, and beautiful UI/UX design.

The application leverages modern Android architecture patterns including MVVM, Repository Pattern, and Coroutines for efficient, responsive performance. With support for multiple audio formats and advanced controls, Aura Music is your ultimate companion for all your music needs.

### Why Choose Aura Music?

✨ **Premium Experience** - Professionally designed interface with smooth animations  
⚡ **Lightning Fast** - Optimized performance with minimal battery consumption  
🔐 **Secure** - Privacy-first approach with no unnecessary permissions  
🎨 **Customizable** - Dark/Light themes and personalization options  
🌐 **Cloud Ready** - Prepared for future streaming capabilities  

---

## ✨ Key Features

### 🎵 Audio Playback & Format Support
- **Multi-Format Support** - MP3, WAV, FLAC, OGG, M4A, AAC, and more
- **High-Fidelity Playback** - Crystal clear audio with minimal compression artifacts
- **Gapless Playback** - Seamless transitions between tracks
- **Audio Visualization** - Real-time frequency spectrum display
- **Equalizer** - 10-band equalizer with presets (Bass Boost, Treble, Classical, Pop, etc.)

### 📱 User Interface & UX
- **Material Design 3** - Modern, cohesive UI following Google's latest design standards
- **Dark & Light Themes** - Toggle between themes for comfortable viewing
- **Gesture Controls** - Swipe, pinch, and tap gestures for intuitive control
- **Smooth Animations** - 60 FPS animations for fluid user experience
- **Responsive Design** - Optimized for phones, tablets, and foldable devices

### 🎼 Music Library Management
- **Smart Library Scanning** - Automatic detection and organization of local music files
- **Advanced Search** - Search by artist, album, genre, or lyrics
- **Playlist Creation** - Create, edit, and manage custom playlists
- **Smart Playlists** - Auto-generated playlists based on listening habits
- **Favorite Marking** - Quick access to your favorite tracks
- **Genre Classification** - Automatic genre detection and sorting

### 🎚️ Playback Controls & Features
- **Shuffle Modes** - Random, smart shuffle, and sequential shuffle
- **Repeat Options** - Repeat all, repeat one, no repeat
- **Queue Management** - View and edit current playback queue
- **Seek & Progress** - Accurate seeking with visual scrubber
- **Sleep Timer** - Auto-stop playback after specified duration
- **Speed Control** - Adjust playback speed (0.5x to 2.0x)

### 📊 Statistics & History
- **Play History** - Track your recently played songs
- **Most Played** - View your most-played tracks and artists
- **Listening Statistics** - Detailed insights into your listening habits
- **Last Played Info** - Remember where you left off
- **Total Playtime** - Aggregate statistics of your music consumption

### 🎯 Recommendations & Discovery
- **AI-Powered Recommendations** - Get song suggestions based on your taste
- **Similar Artists** - Discover artists similar to your favorites
- **Trending Tracks** - Stay updated with trending music
- **Genre Discovery** - Explore new music by genre

### 🔔 Notifications & Alerts
- **Now Playing Notification** - Always visible with playback controls
- **Lockscreen Controls** - Control playback from lock screen
- **Status Bar Integration** - Quick access to media controls
- **Update Alerts** - Stay informed about app updates

### 🎙️ Advanced Features
- **Lyrics Display** - Synchronized lyrics for enhanced enjoyment
- **Metadata Editor** - Edit song information (title, artist, album art)
- **Album Art Display** - Beautiful album artwork presentation
- **Artist Information** - View detailed artist profiles
- **Crossfade Between Tracks** - Smooth transitions between songs

### 🌐 Connectivity & Sync
- **Bluetooth Support** - Connect to Bluetooth speakers and headphones
- **Headphone Controls** - Use physical buttons for playback control
- **Multi-Device Sync** - Sync playlists across devices (future feature)
- **Cloud Storage Ready** - Infrastructure for cloud music integration

### 🔒 Privacy & Security
- **No Ads** - Ad-free listening experience
- **Offline Mode** - Works without internet connection
- **Minimal Permissions** - Only required permissions requested
- **Data Privacy** - Your music library stays private
- **No Tracking** - Respect for user privacy

---

## 🖥️ System Requirements

### Minimum Requirements
```
Android Version: Android 7.0 (API Level 24) or higher
RAM: 2 GB minimum (4 GB recommended)
Storage: 50 MB free space for installation
Processor: ARM v7 or ARM64 architecture
```

### Recommended Requirements
```
Android Version: Android 12+ (API Level 31+)
RAM: 4 GB or higher
Storage: 100 MB+ for optimal performance
Processor: Modern 64-bit processor
Display: 1080p or higher resolution
```

### Supported Devices
- ✅ Smartphones (All sizes)
- ✅ Tablets (7" - 12"+)
- ✅ Foldable Devices (Galaxy Z series)
- ✅ Wear OS (with companion app)

---

## 🏗️ Architecture & Technology Stack

### Architecture Pattern
```
┌─────────────────────────────────────┐
│      UI Layer (Jetpack Compose)     │
├─────────────────────────────────────┤
│  ViewModel (State Management)       │
├─────────────────────────────────────┤
│  Repository Pattern (Data Layer)    │
├─────────────────────────────────────┤
│  Room Database & Local Storage      │
└─────────────────────────────────────┘
```

### Technology Stack

**Language & Framework**
- **Kotlin** 1.9.0+ - Modern, concise, and safe programming language
- **Coroutines** - Efficient asynchronous programming model

**Android Architecture Components**
- **Jetpack Compose** - Modern UI toolkit for declarative UIs
- **ViewModel** - Manage UI state and lifecycle
- **LiveData** - Observable data holder with lifecycle awareness
- **Room Database** - Local SQLite database abstraction

**Multimedia**
- **ExoPlayer** - Powerful media player library for Android
- **MediaSession** - Control playback from external sources
- **AudioManager** - Android audio system integration

**Dependency Injection**
- **Hilt** - Compile-time safe dependency injection

**Networking & APIs**
- **Retrofit** - Type-safe REST client for API calls
- **OkHttp** - HTTP client with caching and interceptors
- **Gson** - JSON serialization/deserialization

**Database & Storage**
- **Room Database** - Compile-time verified SQL queries
- **DataStore** - Modern preferences storage
- **SharedPreferences** - Lightweight key-value storage

**Async & Threading**
- **Kotlin Coroutines** - Lightweight threads for background tasks
- **WorkManager** - Reliable background job scheduling

**Monitoring & Analytics**
- **Firebase Analytics** - Track user behavior (optional)
- **Crashlytics** - Error tracking and reporting

**Build & Deployment**
- **Gradle 8.0+** - Advanced build automation
- **Android Gradle Plugin** - Official Android build tools

---

## 📥 Installation & Setup Guide

### Step 1: Prerequisites Installation

Before starting, ensure you have the following tools installed:

**1.1 Android Studio (Latest Version)**
- Download from: https://developer.android.com/studio
- Installation time: ~10-15 minutes
- Includes Android SDK and emulator tools

**1.2 Java Development Kit (JDK)**
```bash
# Verify Java installation
java -version
# Output should be Java 11 or higher
```

**1.3 Git (Version Control)**
```bash
# Verify Git installation
git --version
```

### Step 2: Clone the Repository

**Option A: Using Git Command**
```bash
git clone https://github.com/Venom-X07/Aura-Music.git
cd Aura-Music
```

**Option B: Using Android Studio**
1. Open Android Studio
2. Click **File** → **New** → **Project from Version Control**
3. Enter repository URL: `https://github.com/Venom-X07/Aura-Music.git`
4. Click **Clone**

### Step 3: Open Project in Android Studio

1. **Launch Android Studio**
2. **Select** "Open an existing Android Studio project"
3. **Navigate** to the cloned Aura-Music directory
4. **Click** "Open"
5. **Wait** for Android Studio to initialize and index files (2-5 minutes)

### Step 4: Configure Project Settings

**4.1 Sync Gradle Files**
```
- Android Studio will automatically prompt to sync
- Click "Sync Now" to download dependencies
- This may take 3-10 minutes on first run
```

**4.2 Accept SDK Licenses** (if prompted)
```
Tools → SDK Manager → SDK Tools → Accept Licenses → Apply → OK
```

### Step 5: Environment Configuration

**5.1 Create Environment File**
```bash
# In project root directory
touch .env
```

**5.2 Edit `.env` File**
```properties
# API Configuration
GEMINI_API_KEY=your_api_key_here
API_BASE_URL=https://api.example.com
ENVIRONMENT=development
```

**5.3 Reference `.env.example`**
```bash
# View template
cat .env.example
# Copy to .env and fill in values
cp .env.example .env
```

### Step 6: Modify Build Configuration

**6.1 Open `build.gradle.kts` (App Module)**
- Locate the line: `signingConfig = signingConfigs.getByName("debugConfig")`
- **Comment out or remove** this line for debug builds

**6.2 Build Configuration Details**
```kotlin
android {
    compileSdk = 34
    minSdk = 24
    targetSdk = 34
    
    // Rest of configuration...
}
```

### Step 7: Setup Virtual Device (Emulator)

**7.1 Create Virtual Device**
```
1. Android Studio → Tools → Device Manager
2. Click "Create Device"
3. Select Device Type: "Phone" or "Tablet"
4. Choose System Image: API 34 (recommended)
5. Configure: RAM (4GB), Storage (2GB)
6. Finish
```

**7.2 Or Connect Physical Device**
```
1. Enable Developer Mode: Settings → About → Build Number (tap 7 times)
2. Enable USB Debugging: Settings → Developer Options → USB Debugging
3. Connect via USB cable
4. Accept USB debugging permission on device
```

### Step 8: Build and Run

**8.1 Build Project**
```bash
# Option 1: Using Gradle command
./gradlew build

# Option 2: Using Android Studio
Build → Make Project (Ctrl+F9)
```

**8.2 Run Application**
```bash
# Option 1: Using Gradle
./gradlew installDebug

# Option 2: Using Android Studio
Click "Run" button or press Shift+F10
```

**8.3 Wait for Installation**
- Build time: 2-5 minutes (first build takes longer)
- Installation time: 30-60 seconds
- App will launch automatically

---

## 🔨 Build & Compilation

### Understanding the Build System

Aura Music uses **Gradle** as the build automation system with Kotlin DSL for configuration.

### Build Types

**Debug Build**
```gradle
buildTypes {
    debug {
        debuggable = true
        minifyEnabled = false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

**Release Build**
```gradle
buildTypes {
    release {
        debuggable = false
        minifyEnabled = true
        shrinkResources = true
        signingConfig = signingConfigs.release
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

### Build Commands

**Development Build**
```bash
# Full build
./gradlew clean build

# Debug APK
./gradlew assembleDebug

# Install and run
./gradlew installDebug runDebug
```

**Production Build**
```bash
# Release build
./gradlew assembleRelease

# With signing
./gradlew bundleRelease

# ProGuard obfuscation
./gradlew -Pandroid.enableR8=true bundleRelease
```

### Build Optimization

**Enable Parallel Builds**
```gradle
org.gradle.parallel=true
org.gradle.workers.max=4
```

**Enable Build Cache**
```gradle
org.gradle.caching=true
```

**Reduce APK Size**
```gradle
android {
    packagingOptions {
        exclude 'META-INF/proguard/androidx-*.pro'
        exclude 'META-INF/licenses/**'
    }
}
```

### Generated Artifacts

- **APK** (APK): `app/build/outputs/apk/debug/app-debug.apk`
- **Bundle** (AAB): `app/build/outputs/bundle/release/app-release.aab`
- **Mapping**: `app/build/outputs/mapping/release/mapping.txt`

---

## ⚙️ Configuration

### Application Configuration

**app/build.gradle.kts**
```kotlin
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.auraplayer.music"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
```

### Dependencies Configuration

**build.gradle.kts (Project Level)**
```kotlin
plugins {
    id("com.android.application") version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
}
```

**build.gradle.kts (App Level)**
```kotlin
dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    
    // UI - Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    
    // Media Player
    implementation("androidx.media3:media3-exoplayer:1.1.0")
    
    // Database
    implementation("androidx.room:room-runtime:2.5.2")
    
    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.46")
    
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
}
```

### Runtime Configuration

**AndroidManifest.xml Permissions**
```xml
<manifest>
    <!-- Audio playback permissions -->
    <uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
    
    <!-- Network permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    
    <!-- Device control -->
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    
    <!-- Storage (for Android 12+) -->
    <uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
</manifest>
```

---

## 🎯 Comprehensive Features Guide

### 1. Music Library Management

#### Automatic Scanning
```kotlin
// Background music discovery
- Scans device storage on app launch
- Monitors folders for new files
- Updates in real-time
- Supports external storage (SD cards)
```

#### Organization Options
- **By Artist** - Group by artist name
- **By Album** - Organize by album
- **By Genre** - Category wise organization
- **By Playlist** - Custom grouped playlists
- **Recently Added** - Newest files first
- **Recently Played** - Recently accessed tracks

### 2. Playback Control System

#### Queue Management
```
- View up to 1000 upcoming tracks
- Reorder tracks in queue
- Remove individual tracks
- Clear entire queue
- Save queue as playlist
```

#### Playback Modes
| Mode | Behavior |
|------|----------|
| **Normal** | Play through queue once |
| **Shuffle** | Random order playback |
| **Repeat All** | Loop entire queue |
| **Repeat One** | Loop current track |
| **Smart Shuffle** | Weighted random (respects favorites) |

### 3. Advanced Audio Features

#### Equalizer System
```
- 10-Band Equalizer (20Hz - 20kHz)
- 15+ Preset Modes:
  ✓ Flat
  ✓ Bass Boost
  ✓ Bass Reduction
  ✓ Treble Boost
  ✓ Treble Reduction
  ✓ Classical
  ✓ Jazz
  ✓ Pop
  ✓ Rock
  ✓ Hip-Hop
  ✓ Electronic
  ✓ Folk
  ✓ Podcast (dialogue enhancement)
  ✓ Voice (speech optimization)
  ✓ Custom (user-created preset)
```

#### Audio Effects
- **Reverb** - Add space and ambience
- **Chorus** - Double voice effect
- **Flange** - Jet-like sweeping sound
- **Phaser** - Oscillating effect
- **Compressor** - Dynamic range control

### 4. Visualization & Display

#### Real-time Audio Visualization
```
- Frequency spectrum display
- Waveform visualization
- Animated bars synchronized with music
- Multiple visualization themes
- Fullscreen visualization mode
```

#### Album Artwork Display
- **High-Resolution** - Up to 2048x2048 pixels
- **Adaptive Colors** - Extract palette from artwork
- **Animated Transitions** - Smooth album art changes
- **Fallback** - Placeholder for missing artwork
- **Blur Effect** - Background blur option

### 5. Playlist Features

#### Playlist Types
| Type | Description |
|------|-------------|
| **User Playlists** | Manually created by user |
| **Auto Playlists** | System-generated (Recent, Favorite, etc.) |
| **Smart Playlists** | AI-generated based on preferences |
| **Collaborative** | Shared playlists (future feature) |

#### Playlist Operations
```
✓ Create new playlist
✓ Add/Remove tracks
✓ Reorder tracks
✓ Rename playlist
✓ Delete playlist
✓ Export as M3U
✓ Import from M3U/PLS
✓ Duplicate playlist
✓ Share via social media
```

### 6. Search & Discovery

#### Search Capabilities
- **Full-Text Search** - Search all metadata
- **Fuzzy Matching** - Find similar matches
- **Filter Options** - By artist, album, genre
- **Recent Searches** - Quick access
- **Search Suggestions** - Auto-complete

#### Discovery Features
- **New Releases** - Track newly added songs
- **Trending Now** - Popular tracks
- **Recommendations** - Based on listening history
- **Similar Artists** - Artist-based suggestions
- **Mood-Based** - Filter by song mood/energy

### 7. Statistics & Analytics

#### Personal Statistics
```
Dashboard shows:
- Total songs in library
- Total artists
- Total albums
- Total listening hours
- Average daily listening time
- Most played artist
- Top 10 songs
- Genre distribution
```

#### Listening History
- **Play Count** - Number of times played
- **Last Played** - When track was last played
- **Total Duration** - Total time spent on track
- **Recently Played** - Last 100 songs
- **Playback Timeline** - Visual playback graph

### 8. Customization Options

#### Theme & Appearance
- **Dark Mode** - AMOLED-friendly dark theme
- **Light Mode** - Comfortable light theme
- **Accent Colors** - 12+ color options
- **Font Sizes** - Adjustable text sizes
- **Compact/Normal/Large** - UI density options

#### User Preferences
```
Settings include:
- Volume normalization
- Audio ducking behavior
- Sleep timer options
- Notification preferences
- Auto-play behavior
- Last played position resume
- Sync preferences
```

### 9. Notification & Lock Screen

#### Now Playing Notification
- Always-on notification during playback
- Playback controls (Previous, Play/Pause, Next)
- Album artwork preview
- Swipe to dismiss
- Tap to open app

#### Lock Screen Controls
- Media controls available on lock screen
- Album artwork display
- Song information
- Skip/Rewind controls
- Fast access from lock screen

### 10. Bluetooth & Connectivity

#### Bluetooth Features
- **Device Pairing** - Easy Bluetooth device connection
- **Multiple Devices** - Quick switching between devices
- **A2DP Profile** - High-quality audio streaming
- **AVRCP Support** - Remote control capability
- **Auto-Connect** - Connect to previously paired device

#### Headphone Controls
- **Physical Button Mapping**:
  - Single press: Play/Pause
  - Double press: Next track
  - Triple press: Previous track
  - Long press: Voice assistant

---

## 📖 Complete Usage Guide

### Initial Setup (First Time)

**Step 1: Grant Permissions**
```
1. Open Aura Music
2. Allow permission to access music files
3. Allow permission for notifications
4. Allow permission for Bluetooth (optional)
5. App will scan your music library
```

**Step 2: Initial Music Scan**
```
- Wait for library scan to complete
- First scan takes 1-2 minutes
- Shows progress: "Scanning music library..."
- Automatic rescan on app startup
```

**Step 3: Customize Settings**
```
Navigate to Settings:
1. Theme preferences (Dark/Light)
2. Accent color selection
3. Notification settings
4. Audio preferences
5. Storage location
```

### Daily Usage

#### Playing Music
```
1. Open Aura Music app
2. Navigate to your music library
3. Select song, artist, album, or playlist
4. Click play icon to start playback
5. Use playback controls to manage playback
```

#### Creating Playlists
```
1. Go to "Playlists" section
2. Tap "New Playlist" button
3. Enter playlist name
4. Search and add songs:
   - Tap song title
   - Click "Add to Playlist"
   - Select your playlist
5. Save playlist
```

#### Managing Library
```
Favorites:
- Long press song
- Select "Add to Favorites"
- Access from "Favorites" section

Sorting:
- Top menu → Sort Options
- Choose: Name, Artist, Date Added, Play Count

Searching:
- Tap search icon
- Type song/artist/album name
- Browse results
- Tap to play
```

#### Using Equalizer
```
1. During playback, tap EQ button
2. Choose from presets OR create custom
3. Adjust 10-band sliders
4. Preview changes in real-time
5. Save custom preset
```

#### Setting Sleep Timer
```
1. Tap menu (three dots)
2. Select "Sleep Timer"
3. Choose duration: 5, 10, 15, 30, 60 minutes
4. Music stops after set time
```

### Advanced Usage

#### Batch Operations
```
Select Multiple Songs:
1. Long press first song
2. Tap additional songs
3. Bulk operations menu:
   - Add to playlist
   - Create station
   - Delete
   - Share
```

#### Import/Export Playlists
```
Import:
1. Prepare M3U or PLS file
2. Settings → Import
3. Select file
4. Confirm import

Export:
1. Select playlist
2. Menu → Export
3. Choose format (M3U/PLS)
4. Save to location
```

#### Lyrics Viewing
```
1. During playback, tap song details
2. Scroll to "Lyrics" tab
3. Read synchronized lyrics
4. Tap to seek to lyric time
5. Tap share to share lyrics
```

---

## 📂 Project Structure

```
Aura-Music/
├── app/                           # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/auraplayer/music/
│   │   │   │   ├── ui/                      # UI Layer
│   │   │   │   │   ├── screens/            # Jetpack Compose screens
│   │   │   │   │   ├── components/         # Reusable UI components
│   │   │   │   │   └── theme/              # Theme configuration
│   │   │   │   │
│   │   │   │   ├── viewmodel/              # ViewModel classes
│   │   │   │   │   ├── MusicViewModel.kt
│   │   │   │   │   ├── PlaylistViewModel.kt
│   │   │   │   │   └── PlayerViewModel.kt
│   │   │   │   │
│   │   │   │   ├── data/                   # Data Layer
│   │   │   │   │   ├── database/          # Room Database
│   │   │   │   │   ├── repository/        # Repository classes
│   │   │   │   │   ├── datasource/        # Data sources
│   │   │   │   │   └── model/             # Data models
│   │   │   │   │
│   │   │   │   ├── domain/                # Business Logic
│   │   │   │   │   ├── usecase/           # Use cases
│   │   │   │   │   └── entity/            # Domain entities
│   │   │   │   │
│   │   │   │   ├── service/               # Background services
│   │   │   │   │   ├── MediaPlayerService.kt
│   │   │   │   │   └── MusicScanService.kt
│   │   │   │   │
│   │   │   │   ├── utils/                 # Utility functions
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── Extensions.kt
│   │   │   │   │   └── Helpers.kt
│   │   │   │   │
│   │   │   │   ├── di/                    # Dependency Injection
│   │   │   │   │   └── Module.kt
│   │   │   │   │
│   │   │   │   └── MainActivity.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/              # Images, icons, shapes
│   │   │   │   ├── layout/                # Legacy XML layouts
│   │   │   │   ├── values/                # Resources (strings, colors, styles)
│   │   │   │   ├── values-night/          # Dark theme resources
│   │   │   │   └── mipmap/                # App icons
│   │   │   │
│   │   │   └── AndroidManifest.xml        # App configuration
│   │   │
│   │   ├── test/                          # Unit tests
│   │   │   └── java/...
│   │   │
│   │   └── androidTest/                   # UI tests
│   │       └── java/...
│   │
│   ├── build.gradle.kts                   # App-level Gradle config
│   └── proguard-rules.pro                 # ProGuard obfuscation rules
│
├── gradle/
│   ├── wrapper/
│   │   └── gradle-wrapper.jar
│   └── wrapper/gradle-wrapper.properties
│
├── build.gradle.kts                       # Project-level Gradle config
├── settings.gradle.kts                    # Gradle settings
├── gradlew                                # Gradle wrapper script
├── gradlew.bat                            # Gradle wrapper batch file
│
├── .env.example                           # Environment variables template
├── .gitignore                             # Git ignore rules
├── README.md                              # This file
└── LICENSE                                # MIT License
```

### Key Directories Explained

**ui/** - User Interface
- Compose screens for different app sections
- Custom UI components and composables
- Theme and styling definitions

**viewmodel/** - State Management
- Manages UI state and logic
- Handles user interactions
- Communicates with repository layer

**data/** - Data Management
- Room Database for local storage
- Repository pattern implementation
- API client configuration
- Data models and entities

**service/** - Background Tasks
- Media playback service
- Music file scanning service
- Notification handling

**di/** - Dependency Injection
- Hilt module configuration
- Singleton definitions
- Provider setup

---

## 💻 Development

### Development Environment Setup

**Recommended IDE Settings**
```
- Kotlin Code Style: Automatic formatting
- Enable Live Templates
- Code Analysis: Set to Most Strict
- Git Integration: Enable
```

### Code Style Guidelines

**Kotlin Naming Conventions**
```kotlin
// Classes & Interfaces - PascalCase
class MusicPlayer { }
interface AudioService { }

// Functions & Variables - camelCase
fun playMusic() { }
val musicLibrary: List<Song> = listOf()

// Constants - UPPER_SNAKE_CASE
const val MAX_VOLUME = 100

// Private properties - _camelCase
private val _playlistCount = MutableLiveData<Int>()
```

**File Organization**
```kotlin
// 1. Package statement
package com.auraplayer.music.ui.screens

// 2. Imports
import androidx.compose.runtime.Composable

// 3. Constants
private const val TAG = "MusicScreen"

// 4. Main class/function
@Composable
fun MusicScreen() { }

// 5. Supporting functions
private fun formatDuration(ms: Long): String { }
```

### Testing

**Unit Tests**
```bash
# Run unit tests
./gradlew test

# Run specific test class
./gradlew test --tests MusicPlayerTest

# Run with coverage
./gradlew testDebugUnitTest --coverage
```

**UI Tests**
```bash
# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.auraplayer.music.MusicScreenTest
```

**Test Structure**
```kotlin
@RunWith(AndroidJUnit4::class)
class MusicPlayerTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testPlaybackControl() {
        // Test implementation
    }
}
```

### Debugging

**Enable Debug Logging**
```kotlin
// In BuildConfig
debuggable = true

// In code
if (BuildConfig.DEBUG) {
    Log.d(TAG, "Debug message")
}
```

**Android Studio Debugger**
```
1. Set breakpoint (click line number)
2. Run → Debug 'app'
3. Execution pauses at breakpoint
4. Step through code: F10 (Step Over), F11 (Step Into)
5. Inspect variables in Variables panel
```

**Network Debugging**
```
1. Android Studio → View → Tool Windows → Logcat
2. Filter: `tag:okhttp` or `tag:retrofit`
3. Monitor network requests and responses
4. Check request/response headers and body
```

### Performance Profiling

**CPU Profiler**
```
1. Run app in debug mode
2. Android Studio → Profiler
3. Click CPU graph
4. Select timeframe to analyze
5. View call stack and time distribution
```

**Memory Profiler**
```
1. Open Profiler tool
2. Click Memory graph
3. Monitor memory usage
4. Force garbage collection
5. Detect memory leaks
```

**Battery Profiler**
```
1. Profiler → Energy
2. Monitor power consumption
3. Identify power-hungry operations
4. Optimize battery usage
```

---

## 🌐 API Integration

### Gemini API Integration

**Configuration**
```kotlin
// In .env file
GEMINI_API_KEY=your_api_key_here
API_BASE_URL=https://generativelanguage.googleapis.com/v1beta/

// In ApiModule.kt
@Provides
@Singleton
fun provideGeminiService(): GeminiService {
    return retrofit.create(GeminiService::class.java)
}
```

**Usage Example**
```kotlin
// Get AI recommendations
viewModel.getRecommendations(userPreferences)

// Generate lyrics
viewModel.generateLyrics(songName, artist)

// Get song insights
viewModel.getSongAnalysis(trackData)
```

### External APIs (Future Integration)

**Spotify Integration**
- OAuth authentication
- Playlist synchronization
- Track metadata enrichment

**Last.fm Integration**
- Scrobbling support
- User statistics
- Similar artist discovery

**Lyrics API**
- Real-time lyric fetching
- Synchronization support

---

## ⚡ Performance Optimization

### Memory Optimization

**Bitmap Handling**
```kotlin
// Load image with size constraints
val options = BitmapFactory.Options()
options.inSampleSize = calculateInSampleSize(reqWidth, reqHeight)
val bitmap = BitmapFactory.decodeFile(imagePath, options)
```

**Coroutine Usage**
```kotlin
// Offload heavy operations to background
viewModelScope.launch(Dispatchers.Default) {
    val result = heavyComputation()
    withContext(Dispatchers.Main) {
        updateUI(result)
    }
}
```

### Disk I/O Optimization

**Batch Database Operations**
```kotlin
// Instead of individual inserts
songsDao.insertAll(songs) // Faster

// Use transactions
database.withTransaction {
    songsDao.insert(song)
    playlistDao.insert(playlist)
}
```

### Network Optimization

**Caching Strategy**
```kotlin
// Configure OkHttp cache
val cacheSize = (5 * 1024 * 1024).toLong() // 5MB
val cache = Cache(cacheDir, cacheSize)

val httpClient = OkHttpClient.Builder()
    .cache(cache)
    .build()
```

**Image Caching**
```kotlin
// Implement image cache
val cache = LruCache<String, Bitmap>(size)

fun getImage(url: String): Bitmap {
    return cache[url] ?: loadAndCache(url)
}
```

---

## 🔐 Security Features

### Permission Handling

**Runtime Permissions (Android 6.0+)**
```kotlin
if (ContextCompat.checkSelfPermission(context, 
    Manifest.permission.READ_MEDIA_AUDIO) 
    != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(activity,
        arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 
        REQUEST_CODE)
}
```

### Data Security

**Encryption**
```kotlin
// Encrypt sensitive data
val encryptedPreferences = EncryptedSharedPreferences.create(
    context,
    "secret_shared_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

### API Security

**Secure API Communication**
```kotlin
// Use certificate pinning
val certificatePinner = CertificatePinner.Builder()
    .add("api.example.com", "sha256/...")
    .build()

val httpClient = OkHttpClient.Builder()
    .certificatePinner(certificatePinner)
    .build()
```

---

## 🔧 Troubleshooting

### Common Issues & Solutions

**Issue: Gradle Sync Fails**
```
Solution:
1. File → Invalidate Caches
2. Restart Android Studio
3. Delete .gradle folder: rm -rf ~/.gradle
4. Try syncing again
```

**Issue: App Crashes on Startup**
```
Solution:
1. Check logcat for error messages
2. Verify permissions in AndroidManifest.xml
3. Ensure .env file exists with API keys
4. Check minimum SDK compatibility
```

**Issue: Music Files Not Detected**
```
Solution:
1. Verify READ_MEDIA_AUDIO permission granted
2. Check file format is supported
3. Ensure files are in standard music directories
4. Try manual rescan: Settings → Rescan Library
5. Restart app
```

**Issue: Playback Stutters or Lags**
```
Solution:
1. Close background apps
2. Clear app cache: Settings → Apps → Aura → Clear Cache
3. Reduce audio quality if using streaming
4. Check device storage space (needs 100MB+)
5. Update to latest app version
```

**Issue: Battery Drain**
```
Solution:
1. Disable location services while using app
2. Reduce equalizer complexity
3. Disable visualizations if not needed
4. Check for background sync issues
5. Update app and Android OS
```

### Debug Mode

**Enable Debug Logging**
```bash
# In build.gradle.kts
buildTypes {
    debug {
        debuggable = true
        buildConfigField("boolean", "DEBUG_LOGS", "true")
    }
}
```

**View Logs in Android Studio**
```
1. Android Studio → Logcat
2. Filter by app package name
3. Search specific tags
4. Export logs for analysis
```

---

## 🤝 Contributing

We welcome contributions from the community! Here's how to get involved:

### Contribution Guidelines

**1. Fork Repository**
- Click "Fork" on GitHub
- Clone your fork: `git clone [your-fork-url]`

**2. Create Feature Branch**
```bash
git checkout -b feature/AuraMusic-NewFeature
git checkout -b bugfix/issue-description
```

**3. Make Changes**
- Follow code style guidelines
- Write clean, documented code
- Test thoroughly before committing

**4. Commit Changes**
```bash
git add .
git commit -m "feat: Add new feature description"
# or
git commit -m "fix: Fix bug description"
```

**5. Push to GitHub**
```bash
git push origin feature/AuraMusic-NewFeature
```

**6. Create Pull Request**
- Go to GitHub repository
- Click "New Pull Request"
- Describe your changes
- Wait for review and feedback

### Reporting Issues

**Before Reporting**
- Check existing issues
- Search for similar problems
- Update to latest version

**Issue Template**
```markdown
**Describe the bug:**
[Clear description of issue]

**Steps to reproduce:**
1. First step
2. Second step

**Expected behavior:**
[What should happen]

**Actual behavior:**
[What actually happens]

**Environment:**
- Device: [e.g., Samsung Galaxy S21]
- Android Version: [e.g., Android 12]
- App Version: [e.g., 1.0.0]
```

---

## 📄 License

Aura Music is released under the **MIT License**.

```
MIT License

Copyright (c) 2024 Venom-X07

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

See [LICENSE](LICENSE) file for full details.

---

## 📞 Support

### Getting Help

**Documentation**
- Check this README thoroughly
- Review troubleshooting section
- Explore inline code documentation

**Community Support**
- GitHub Issues: Report bugs and feature requests
- Discussions: Ask questions and share ideas
- Stack Overflow: Tag questions with `aura-music`

**Direct Contact**
- 📧 Email: aitechengineer001@gmail.com
- 🐦 Twitter: [@VenomX07](https://twitter.com/VenomX07)
- 💻 GitHub: [@Venom-X07](https://github.com/Venom-X07)

### Bug Bounty

Found a security vulnerability? Please report it responsibly to:
- Email: security@auraplayer.dev
- Do not disclose publicly until patch is released

---

## 🎉 Acknowledgments

Special thanks to:
- Android Development Community
- Jetpack Compose Team
- ExoPlayer Contributors
- All Contributors and Users

---

## 📈 Roadmap

### Upcoming Features
- ✅ Cloud Storage Integration
- ✅ Collaborative Playlists
- ✅ Gapless Playback
- ✅ Wear OS Support
- ✅ Multi-Device Sync
- ✅ Advanced Lyrics Integration
- ✅ Music Recognition
- ✅ Social Sharing Features

### Future Enhancements
- Integration with Spotify/Apple Music
- Voice Command Support
- Video Music Support
- Podcast Integration
- MusicBrainz Integration

---

<div align="center">

## 🎵 **Enjoy Your Music with Aura Music!** 🎵

### Made with ❤️ by Venom-X07

**[⬆ Back to Top](#-aura-music)**

</div>
