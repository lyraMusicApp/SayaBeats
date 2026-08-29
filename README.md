# Saya Beats

<div align="center">

![Saya Beats](https://raw.githubusercontent.com/lyraMusicApp/SayaBeats/main/androidApp/src/main/ic_launcher-playstore.png)

**A Next-Generation Liquid Glass Music Streaming Experience**

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Desktop-green.svg)](https://github.com/lyraMusicApp/SayaBeats)
[![Release](https://img.shields.io/badge/Release-v1.3.0-purple.svg)](https://github.com/lyraMusicApp/SayaBeats/releases)

</div>

---

## 🌟 Highlights & Features

### 🔮 Liquid Glass (Glassmorphism) UI
- Full-app liquid glass frosted aesthetics with real-time backdrop blur (`hazeEffect` and Android hardware backdrop).
- Specular gradient borders, glowing cyan & purple neon halos, and ambient mesh lighting.
- Premium glass buttons, circular play/pause triggers, and glowing pill chips.

### 🎛️ Dual Homepage Layout Switcher
Switch dynamically in **Settings -> User Interface -> Homepage Layout Style**:
1. **Style 1 (Curated Carousel)**:
   - Dynamic user greeting and avatar header with frosted quick action buttons.
   - Horizontal sliding **"Curated & Trending" featured card carousel** on mobile with fast controls (Play, Like, Download, More).
   - Horizontal filter pills: All, New Release, Trending, Top, Relax, Sleep, Workout, Party, Focus, Romance, Sad.
   - Curated song rows with timestamps and circular glass play buttons.
2. **Style 2 (Minimalist List & Dock)**:
   - Sleek top header with down chevron, drag handle pill, and options menu.
   - Minimalist vertical track list with high-res thumbnails and track timestamps.
   - Docked floating Liquid Glass Mini-Player card above the bottom navigation.

### 🎧 Dual Player Screen Switcher
Switch dynamically in **Settings -> User Interface -> Player Screen Style**:
1. **Style 1 ("Arcane" Vinyl Disc & Outer Ring)**:
   - Rotating vinyl album artwork disc with glowing ambient neon aura.
   - Circular progress ring with glowing cyan scrubber knob and live time display.
   - Frosted glass player controls row (Shuffle, Prev, Big Glass Play/Pause, Next, Repeat).
   - "Next Songs" upcoming queue preview.
2. **Style 2 ("Skin" Coverflow Glass Lens)**:
   - 3D Cover-flow carousel disc player with side track peeking.
   - Frosted glass lens outer bezel ring.
   - Bottom arc progress indicator and clean controls row.
   - Floating 5-icon liquid glass bottom dock (Home, Queue, Center Glowing Visualizer Orb, Equalizer, Notification).

### ⚙️ Modern Settings & Preferences
- Redesigned liquid glass card layout.
- High-fidelity streaming quality (320kbps), downloads, video playback, crossfade, DJ mode, Equalizer, and SponsorBlock.
- Strictly clean vector UI icons throughout the entire app—no emojis.

---

## 🚀 Building & Packaging

### Prerequisites
- JDK 21
- Android SDK (API 34+)

### Build Release APK
Run the following command in the root directory:

```bash
# Set Android SDK path if needed
export ANDROID_HOME=$HOME/Android/Sdk

# Build single Release APK
./gradlew :androidApp:assembleRelease
```

The output Release APK will be located at:
```text
androidApp/build/outputs/apk/release/androidApp-universal-release-unsigned.apk
```

---

## 🛠️ Tech Stack
- **Kotlin Multiplatform (KMP)** & **Compose Multiplatform (CMP)**
- **Android Media3 (ExoPlayer)** for seamless background playback
- **Koin** for Dependency Injection
- **Coil 3** for asynchronous image loading and caching
- **Haze & Kyant Backdrop** for liquid glass backdrop blur effects
- **Kotlinx Coroutines & Flow** for reactive state management

---

## 📄 License & Attribution
Saya Beats is released under the [GNU General Public License v3.0](LICENSE).  
Based on SimpMusic by Tuan Minh Nguyen Duc (maxrave-dev) and contributors.
Rebranded, enhanced, and maintained for SHNWAZ.
