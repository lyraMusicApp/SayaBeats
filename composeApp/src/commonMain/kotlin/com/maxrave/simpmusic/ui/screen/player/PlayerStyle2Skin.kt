package com.maxrave.simpmusic.ui.screen.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.SpatialAudio
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.maxrave.domain.data.model.browse.album.Track
import com.maxrave.domain.data.model.streams.TimeLine
import com.maxrave.domain.mediaservice.handler.RepeatState
import com.maxrave.simpmusic.ui.component.glass.GlassBorderGradient
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassBackground
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassCard
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassIconButton
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassPlayPauseButton
import com.maxrave.simpmusic.ui.theme.typo

private fun formatMsTime(ms: Long): String {
    if (ms <= 0L) return "00:00"
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    val minStr = if (min < 10) "0$min" else "$min"
    val secStr = if (sec < 10) "0$sec" else "$sec"
    return "$minStr:$secStr"
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerStyle2Skin(
    track: Track?,
    timeline: TimeLine,
    isPlaying: Boolean,
    queueTracks: List<Track>,
    currentQueueIndex: Int,
    isShuffle: Boolean,
    repeatMode: RepeatState,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Float) -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onDismiss: () -> Unit,
    onTrackSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = track?.title ?: "Skin"
    val artist = track?.artists?.joinToString(", ") { it.name } ?: "Flume"
    val artworkUrl = track?.thumbnails?.lastOrNull()?.url ?: ""

    val progress = if (timeline.total > 0L) {
        (timeline.current.toFloat() / timeline.total.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val currentTimeStr = formatMsTime(timeline.current)
    val totalTimeStr = formatMsTime(timeline.total)

    val pagerTracks = remember(queueTracks, track) {
        if (queueTracks.isNotEmpty()) queueTracks else if (track != null) listOf(track) else emptyList()
    }

    val pagerState = rememberPagerState(
        initialPage = if (currentQueueIndex in pagerTracks.indices) currentQueueIndex else 0,
        pageCount = { pagerTracks.size.coerceAtLeast(1) }
    )

    LaunchedEffect(currentQueueIndex) {
        if (currentQueueIndex in pagerTracks.indices && pagerState.currentPage != currentQueueIndex) {
            pagerState.animateScrollToPage(currentQueueIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentQueueIndex && pagerState.currentPage in pagerTracks.indices) {
            onTrackSelect(pagerState.currentPage)
        }
    }

    LiquidGlassBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Collapse chevron, drag pill, 3 dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassIconButton(
                    icon = Icons.Rounded.KeyboardArrowDown,
                    size = 40.dp,
                    iconSize = 22.dp,
                    onClick = onDismiss
                )

                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.40f))
                )

                LiquidGlassIconButton(
                    icon = Icons.Rounded.MoreVert,
                    size = 40.dp,
                    iconSize = 20.dp,
                    onClick = {}
                )
            }

            // Coverflow Disc / Carousel with side peek
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                if (pagerTracks.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 70.dp),
                        pageSpacing = 16.dp,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val pageTrack = pagerTracks.getOrNull(page)
                        val pageArtwork = pageTrack?.thumbnails?.lastOrNull()?.url ?: artworkUrl
                        val isCurrent = page == pagerState.currentPage

                        val scale by animateFloatAsState(
                            targetValue = if (isCurrent) 1.0f else 0.82f,
                            animationSpec = tween(300),
                            label = "discScale"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(scale),
                            contentAlignment = Alignment.Center
                        ) {
                            // Ambient Glow behind the active disc
                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .size(240.dp)
                                        .blur(60.dp)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(
                                                    Color(0xFF8B5CF6).copy(alpha = 0.40f),
                                                    Color(0xFFEC4899).copy(alpha = 0.25f),
                                                    Color.Transparent
                                                )
                                            ),
                                            shape = CircleShape
                                        )
                                )
                            }

                            // Frosted Glass Lens Outer Bezel
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x351E293B))
                                    .border(
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = 6.dp,
                                            brush = Brush.linearGradient(
                                                listOf(
                                                    Color.White.copy(alpha = 0.40f),
                                                    Color.White.copy(alpha = 0.08f),
                                                    Color(0xFFC084FC).copy(alpha = 0.35f)
                                                )
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                // Album Artwork Disc
                                Box(
                                    modifier = Modifier
                                        .size(210.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalPlatformContext.current)
                                            .data(pageArtwork)
                                            .diskCachePolicy(CachePolicy.ENABLED)
                                            .crossfade(300)
                                            .build(),
                                        contentDescription = pageTrack?.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Title, Artist and Heart Like Button Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = typo().headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = artist,
                        style = typo().bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = Color(0xFF94A3B8),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Heart Like Icon Button
                LiquidGlassIconButton(
                    icon = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    size = 44.dp,
                    iconSize = 22.dp,
                    tint = if (isLiked) Color(0xFFFF4D6D) else Color.White,
                    onClick = onLikeClick
                )
            }

            // Bottom Progress Bar / Arc with Timestamps
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            ) {
                Slider(
                    value = progress,
                    onValueChange = { onSeek(it) },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(0xFFA855F7),
                        inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = currentTimeStr,
                        style = typo().bodySmall.copy(fontSize = 12.sp),
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = totalTimeStr,
                        style = typo().bodySmall.copy(fontSize = 12.sp),
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // Playback Controls Row: Shuffle, Prev, Play/Pause, Next, Repeat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassIconButton(
                    icon = Icons.Rounded.Shuffle,
                    size = 42.dp,
                    iconSize = 20.dp,
                    tint = if (isShuffle) Color(0xFFC084FC) else Color(0xFF94A3B8),
                    onClick = onToggleShuffle
                )

                LiquidGlassIconButton(
                    icon = Icons.Rounded.SkipPrevious,
                    size = 46.dp,
                    iconSize = 24.dp,
                    onClick = onPrevious
                )

                LiquidGlassPlayPauseButton(
                    isPlaying = isPlaying,
                    size = 68.dp,
                    iconSize = 34.dp,
                    accentGlow = true,
                    onClick = onPlayPause
                )

                LiquidGlassIconButton(
                    icon = Icons.Rounded.SkipNext,
                    size = 46.dp,
                    iconSize = 24.dp,
                    onClick = onNext
                )

                LiquidGlassIconButton(
                    icon = if (repeatMode is RepeatState.One) Icons.Rounded.RepeatOne else Icons.Rounded.Repeat,
                    size = 42.dp,
                    iconSize = 20.dp,
                    tint = if (repeatMode !is RepeatState.None) Color(0xFFC084FC) else Color(0xFF94A3B8),
                    onClick = onToggleRepeat
                )
            }

            // Floating 5-Icon Glass Bottom Dock
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(62.dp),
                shape = CircleShape,
                backgroundColor = Color(0x350F172A),
                borderBrush = GlassBorderGradient
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Home",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() }
                    )

                    Icon(
                        imageVector = Icons.Rounded.QueueMusic,
                        contentDescription = "Queue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )

                    // Center Glowing Visualizer Orb
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
                                )
                            )
                            .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SpatialAudio,
                            contentDescription = "Visualizer",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.Equalizer,
                        contentDescription = "Equalizer",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )

                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = "Notification",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
