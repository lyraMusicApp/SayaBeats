# 🎵 Saya Beats v1.3.0 Release Notes

We are thrilled to announce the **Saya Beats v1.3.0** release featuring a comprehensive **Liquid Glass (Glassmorphism)** redesign, dual customizable **Homepage & Player layout switchers**, modern vector UI icons (no emojis), and performance optimizations.

---

## ✨ What's New

### 🔮 Liquid Glass Aesthetic
- **Frosted Glass Primitives**: Custom glass containers with backdrop blur, specular gradient borders, and ambient colored glows across the entire application.
- **Ambient Gradient Backdrops**: Dynamic dark space meshes with subtle teal, indigo, and violet refraction spots matching modern glassmorphic designs.
- **Glass Buttons & Controls**: Frosted circular and pill action buttons with haptic feedback, ripples, and specular light highlights.

### 📱 Switchable Homepage Layouts
- **Homepage Style 1 (Curated Carousel)**:
  - Header greeting ("Good morning", "Hi, Samantha" / account name) with circular frosted glass quick action buttons.
  - Horizontal swipeable **"Curated & Trending" featured card carousel** on mobile with quick action controls (Play, Like, Download, More) and artwork cards.
  - Category filter pills ("All", "New Release", "Trending", "Top", "Relax", "Workout", "Party", "Focus", "Romance", "Sad", "Sleep").
  - "Curated & Trending" song list with timestamps, visualizer bars, and circular glass play buttons.
- **Homepage Style 2 (Sleek List & Dock)**:
  - Minimalist top bar with collapse chevron, drag handle pill, and 3-dots options menu.
  - Sleek vertical track list with high-res thumbnails, song metadata, and clean dividers.
  - Floating Liquid Glass mini-player dock docked seamlessly above the bottom navigation.

### 🎧 Switchable Player Screens
- **Player Style 1 ("Arcane" Vinyl Disc Ring)**:
  - Central rotating vinyl disc with circular album art disc and ambient blue/purple neon halo.
  - Outer circular progress ring with glowing cyan scrubber knob and live timestamp display (`02:46` / total).
  - Frosted glass player controls row and upcoming "Next Songs" queue preview.
- **Player Style 2 ("Skin" Coverflow Glass Lens)**:
  - Horizontal 3D Cover-flow carousel disc player with side preview track peeking.
  - Frosted glass lens outer bezel ring.
  - Bottom rim progress arc with elapsed time indicator.
  - Floating 5-icon glass bottom dock (Home, Queue, Center Glowing Visualizer Orb, Equalizer, Notification).

### ⚙️ Redesigned Settings
- Categorized liquid glass card sections for Appearance & Layouts, Audio & Playback Quality, Content, Cache, and Backup.
- Instant toggle between Homepage Style 1 & Style 2.
- Instant toggle between Player Style 1 & Player Style 2.
- Pure vector UI icons throughout all menus, dialogs, and headers—strictly no emojis.

---

## 🛠️ Build Artifacts
- **Release APK**: Built using `./gradlew :androidApp:assembleRelease` targeting Android (universal release).
- Output: `androidApp/build/outputs/apk/release/androidApp-universal-release-unsigned.apk` (or signed release APK).

---

## 📜 Credits & License
- **Rebranded & Enhanced by**: SHNWAZ
- **Base Project**: SimpMusic (GPL-3.0 License)
- **Repository**: [https://github.com/lyraMusicApp/SayaBeats.git](https://github.com/lyraMusicApp/SayaBeats.git)
