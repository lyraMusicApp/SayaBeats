package com.maxrave.simpmusic.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.maxrave.common.Config
import com.maxrave.domain.data.model.browse.album.Track
import com.maxrave.domain.mediaservice.handler.PlaylistType
import com.maxrave.domain.mediaservice.handler.QueueData
import com.maxrave.domain.utils.toTrack
import com.maxrave.simpmusic.ui.component.EndOfPage
import com.maxrave.simpmusic.ui.component.HomeShimmer
import com.maxrave.simpmusic.ui.component.glass.GlassBorderGradient
import com.maxrave.simpmusic.ui.component.glass.GlassGlowCyanBorder
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassBackground
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassCard
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassIconButton
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassPill
import com.maxrave.simpmusic.ui.component.home.FeaturedCardCarousel
import com.maxrave.simpmusic.ui.component.home.FeaturedCardData
import com.maxrave.simpmusic.ui.navigation.destination.home.SettingsDestination
import com.maxrave.simpmusic.ui.navigation.destination.library.LibraryDestination
import com.maxrave.simpmusic.ui.theme.typo
import com.maxrave.simpmusic.viewModel.HomeViewModel
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_COMMUTE
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_ENERGIZE
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_FEEL_GOOD
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_FOCUS
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_PARTY
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_RELAX
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_ROMANCE
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_SAD
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_SLEEP
import com.maxrave.simpmusic.viewModel.HomeViewModel.Companion.HOME_PARAMS_WORKOUT
import com.maxrave.domain.extension.now
import com.maxrave.simpmusic.viewModel.SharedViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenStyle1(
    navController: NavController,
    viewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    scrollState: LazyListState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
) {
    val accountInfo by viewModel.accountInfo.collectAsStateWithLifecycle()
    val homeData by viewModel.homeItemList.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val params by viewModel.params.collectAsStateWithLifecycle()
    val nowPlayingData by sharedViewModel.nowPlayingState.collectAsStateWithLifecycle()
    val controllerState by sharedViewModel.controllerState.collectAsStateWithLifecycle()

    val currentHour = now().hour
    val greeting = when (currentHour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..21 -> "Good evening"
        else -> "Good night"
    }

    val displayName = accountInfo?.first ?: "Samantha"
    val avatarUrl = accountInfo?.second ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150"

    val chips = listOf(
        "All" to "",
        "New Release" to "new_release",
        "Trending" to "trending",
        "Top" to "top",
        "Relax" to HOME_PARAMS_RELAX,
        "Sleep" to HOME_PARAMS_SLEEP,
        "Workout" to HOME_PARAMS_WORKOUT,
        "Party" to HOME_PARAMS_PARTY,
        "Focus" to HOME_PARAMS_FOCUS,
        "Romance" to HOME_PARAMS_ROMANCE,
        "Sad" to HOME_PARAMS_SAD,
    )

    // Build Featured Carousel Cards
    val featuredCards = remember(homeData) {
        val quickPicks = homeData.find { it.contents.isNotEmpty() }
        val firstItem = quickPicks?.contents?.firstOrNull()
        listOf(
            FeaturedCardData(
                title = "Discover weekly",
                description = "The original slow instrumental best playlists",
                imageUrl = firstItem?.thumbnails?.lastOrNull()?.url
                    ?: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500",
                gradientColors = listOf(
                    Color(0xFF0284C7),
                    Color(0xFF2563EB),
                    Color(0xFF7C3AED)
                ),
                onClickPlay = {
                    firstItem?.let {
                        val track: Track = it.toTrack()
                        viewModel.setQueueData(
                            QueueData.Data(
                                listTracks = listOf(track),
                                firstPlayedTrack = track,
                                playlistId = "DISCOVER_WEEKLY",
                                playlistName = "Discover Weekly",
                                playlistType = PlaylistType.PLAYLIST
                            )
                        )
                        viewModel.loadMediaItem(track, type = Config.SONG_CLICK)
                    }
                }
            ),
            FeaturedCardData(
                title = "Curated & Trending",
                description = "Top global hits and fresh soundscapes for you",
                imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500",
                gradientColors = listOf(
                    Color(0xFF0F766E),
                    Color(0xFF0284C7),
                    Color(0xFF4F46E5)
                ),
                onClickPlay = {}
            ),
            FeaturedCardData(
                title = "Night Vibes Chill",
                description = "Ambient and lofi beats for late night relaxation",
                imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=500",
                gradientColors = listOf(
                    Color(0xFF6B21A8),
                    Color(0xFF9333EA),
                    Color(0xFFC026D3)
                ),
                onClickPlay = {}
            )
        )
    }

    LiquidGlassBackground {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: User Greeting & Actions
            item(key = "header_greeting") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Avatar & Greeting
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33FFFFFF))
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(avatarUrl)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .crossfade(300)
                                        .build(),
                                    contentDescription = "User Avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column {
                                Text(
                                    text = "Hi, $displayName",
                                    style = typo().titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = greeting,
                                    style = typo().bodySmall.copy(
                                        fontSize = 12.sp
                                    ),
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }

                        // Action buttons: Edit/Settings and Favorite/Heart
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            LiquidGlassIconButton(
                                icon = Icons.Rounded.Settings,
                                size = 42.dp,
                                iconSize = 20.dp,
                                onClick = {
                                    navController.navigate(SettingsDestination)
                                }
                            )

                            LiquidGlassIconButton(
                                icon = Icons.Rounded.Favorite,
                                size = 42.dp,
                                iconSize = 20.dp,
                                tint = Color(0xFFFF4D6D),
                                onClick = {
                                    navController.navigate(LibraryDestination)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Horizontal Filter Pills Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chips.forEach { (label, key) ->
                            val isSelected = (params?.isEmpty() == true && key.isEmpty()) || (params == key && key.isNotEmpty())
                            LiquidGlassPill(
                                text = label,
                                isSelected = isSelected,
                                onClick = {
                                    if (key.isNotEmpty()) {
                                        viewModel.getHomeItemList(key)
                                    } else {
                                        viewModel.getHomeItemList("")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Featured Card Sliding Carousel ("Curated & Trending")
            item(key = "featured_carousel") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Curated & Trending",
                            style = typo().titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                letterSpacing = 0.2.sp
                            ),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    FeaturedCardCarousel(
                        cards = featuredCards,
                        onCardClick = { card ->
                            card.onClickPlay()
                        }
                    )
                }
            }

            // Curated & Trending Songs List ("See all")
            item(key = "curated_songs_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Curated & Trending",
                        style = typo().titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 0.2.sp
                        ),
                        color = Color.White
                    )

                    Text(
                        text = "See all",
                        style = typo().bodySmall.copy(
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.clickable {
                            navController.navigate(LibraryDestination)
                        }
                    )
                }
            }

            // Song list items in frosted glass card rows
            val songItems = homeData.flatMap { it.contents }.filterNotNull().take(15)
            if (songItems.isNotEmpty()) {
                itemsIndexed(songItems, key = { index, item -> "style1_song_${item.videoId}_$index" }) { index, song ->
                    val isPlayingThis = nowPlayingData?.track?.videoId == song.videoId
                    val isCurrentPlaying = isPlayingThis && controllerState.isPlaying

                    GlassSongListItem(
                        title = song.title ?: "New Love song",
                        artist = song.artists?.joinToString(", ") { it.name } ?: "Albert Munsur",
                        thumbnailUrl = song.thumbnails?.lastOrNull()?.url ?: "",
                        duration = "02:46",
                        isPlaying = isCurrentPlaying,
                        isCurrentTrack = isPlayingThis,
                        onClickPlay = {
                            val track: Track = song.toTrack()
                            val allTracks: List<Track> = songItems.map { it.toTrack() }
                            viewModel.setQueueData(
                                QueueData.Data(
                                    listTracks = allTracks,
                                    firstPlayedTrack = track,
                                    playlistId = "HOME_CURATED",
                                    playlistName = "Curated & Trending",
                                    playlistType = PlaylistType.PLAYLIST
                                )
                            )
                            viewModel.loadMediaItem(track, type = Config.SONG_CLICK)
                        },
                        onClickRow = {
                            val track: Track = song.toTrack()
                            val allTracks: List<Track> = songItems.map { it.toTrack() }
                            viewModel.setQueueData(
                                QueueData.Data(
                                    listTracks = allTracks,
                                    firstPlayedTrack = track,
                                    playlistId = "HOME_CURATED",
                                    playlistName = "Curated & Trending",
                                    playlistType = PlaylistType.PLAYLIST
                                )
                            )
                            viewModel.loadMediaItem(track, type = Config.SONG_CLICK)
                        }
                    )
                }
            } else if (loading) {
                item {
                    HomeShimmer()
                }
            }

            item {
                EndOfPage()
            }
        }
    }
}

@Composable
fun GlassSongListItem(
    title: String,
    artist: String,
    thumbnailUrl: String,
    duration: String,
    isPlaying: Boolean,
    isCurrentTrack: Boolean,
    onClickPlay: () -> Unit,
    onClickRow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = if (isCurrentTrack) Color(0x351E293B) else Color(0x18151D30),
        borderBrush = if (isCurrentTrack) GlassGlowCyanBorder else GlassBorderGradient,
        onClick = onClickRow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Square rounded thumbnail
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33000000))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(thumbnailUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(250)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.GraphicEq,
                            contentDescription = "Playing",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title & Artist
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typo().bodyMedium.copy(
                        fontWeight = if (isCurrentTrack) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = if (isCurrentTrack) Color(0xFF38BDF8) else Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = artist,
                    style = typo().bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Timestamp Duration
            Text(
                text = duration,
                style = typo().bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Play / Pause Circle Button
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPlaying) {
                            Brush.linearGradient(
                                listOf(Color(0xFF0284C7), Color(0xFF2563EB))
                            )
                        } else {
                            Brush.linearGradient(
                                listOf(Color(0x35FFFFFF), Color(0x15FFFFFF))
                            )
                        }
                    )
                    .clickable { onClickPlay() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
