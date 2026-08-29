package com.maxrave.simpmusic.ui.screen.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private fun formatMsTime(ms: Long): String {
    if (ms <= 0L) return "00:00"
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    val minStr = if (min < 10) "0$min" else "$min"
    val secStr = if (sec < 10) "0$sec" else "$sec"
    return "$minStr:$secStr"
}

@Composable
fun PlayerStyle1Arcane(
    track: Track?,
    timeline: TimeLine,
    isPlaying: Boolean,
    queueTracks: List<Track>,
    currentQueueIndex: Int,
    isShuffle: Boolean,
    repeatMode: RepeatState,
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
    val title = track?.title ?: "Arcane"
    val artist = track?.artists?.joinToString(", ") { it.name } ?: "Albert Munsur"
    val artworkUrl = track?.thumbnails?.lastOrNull()?.url ?: ""

    val progress = if (timeline.total > 0L) {
        (timeline.current.toFloat() / timeline.total.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val currentTimeStr = formatMsTime(timeline.current)
    val totalTimeStr = formatMsTime(timeline.total)

    // Smooth vinyl continuous rotation when playing
    val infiniteTransition = rememberInfiniteTransition(label = "vinylSpin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LiquidGlassBackground {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Glass Back and Glass Queue/More buttons
            item(key = "header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LiquidGlassIconButton(
                        icon = Icons.Rounded.KeyboardArrowDown,
                        size = 44.dp,
                        iconSize = 24.dp,
                        onClick = onDismiss
                    )

                    LiquidGlassIconButton(
                        icon = Icons.Rounded.QueueMusic,
                        size = 44.dp,
                        iconSize = 22.dp,
                        onClick = {}
                    )
                }
            }

            // Track Title & Artist
            item(key = "title_artist") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = typo().headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            letterSpacing = 0.4.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = artist,
                        style = typo().bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color(0xFF94A3B8),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Rotating Vinyl Disc with Circular Progress Ring & Cyan Scrubber Knob
            item(key = "vinyl_disc_ring") {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .size(310.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Ambient Cyan / Purple Aura Glow behind disc
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .blur(50.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF00E5FF).copy(alpha = 0.35f),
                                        Color(0xFF7C3AED).copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // Vinyl Grooves Outer Body
                    Box(
                        modifier = Modifier
                            .size(255.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF1E293B),
                                        Color(0xFF0F172A),
                                        Color(0xFF030712)
                                    )
                                )
                            )
                            .border(1.5.dp, Color.White.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Central Album Artwork Disc
                        Box(
                            modifier = Modifier
                                .size(145.dp)
                                .rotate(if (isPlaying) rotationAngle else 0f)
                                .clip(CircleShape)
                                .border(3.dp, Color.Black.copy(alpha = 0.7f), CircleShape)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(artworkUrl)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .crossfade(300)
                                    .build(),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Center Spindle Hole
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F172A))
                                    .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                            )
                        }
                    }

                    // Outer Circular Progress Ring Canvas & Interactive Scrubbing
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    val center = Offset(size.width / 2f, size.height / 2f)
                                    val touch = change.position
                                    val angle = atan2(touch.y - center.y, touch.x - center.x)
                                    var degrees = Math.toDegrees(angle.toDouble()).toFloat()
                                    // Adjust so 0 deg is at top (-90)
                                    degrees = (degrees + 90f + 360f) % 360f
                                    val newProgress = (degrees / 360f).coerceIn(0f, 1f)
                                    onSeek(newProgress)
                                }
                            }
                    ) {
                        val strokeWidth = 5.dp.toPx()
                        val diameter = size.minDimension - strokeWidth - 16.dp.toPx()
                        val arcTopLeft = Offset(
                            (size.width - diameter) / 2f,
                            (size.height - diameter) / 2f
                        )
                        val arcSize = Size(diameter, diameter)

                        // Inactive track ring
                        drawArc(
                            color = Color.White.copy(alpha = 0.12f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = arcTopLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // Active Glowing Progress Arc
                        val sweepAngle = progress * 360f
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF00E5FF),
                                    Color(0xFF38BDF8),
                                    Color(0xFF818CF8),
                                    Color(0xFF00E5FF)
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = arcTopLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth + 1.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Glowing Cyan Knob at Scrubber Tip
                        val knobAngleRad = Math.toRadians((sweepAngle - 90.0))
                        val radius = diameter / 2f
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val knobCenter = Offset(
                            x = (center.x + radius * cos(knobAngleRad)).toFloat(),
                            y = (center.y + radius * sin(knobAngleRad)).toFloat()
                        )

                        drawCircle(
                            color = Color(0xFF00E5FF),
                            radius = 8.dp.toPx(),
                            center = knobCenter
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.5.dp.toPx(),
                            center = knobCenter
                        )
                    }

                    // Scrubber Timestamp Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x770F172A))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "$currentTimeStr / $totalTimeStr",
                            style = typo().bodySmall.copy(
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFFE2E8F0)
                        )
                    }
                }
            }

            // Frosted Glass Player Controls Row
            item(key = "player_controls") {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shuffle
                    LiquidGlassIconButton(
                        icon = Icons.Rounded.Shuffle,
                        size = 42.dp,
                        iconSize = 20.dp,
                        tint = if (isShuffle) Color(0xFF00E5FF) else Color(0xFF94A3B8),
                        onClick = onToggleShuffle
                    )

                    // Previous Track
                    LiquidGlassIconButton(
                        icon = Icons.Rounded.SkipPrevious,
                        size = 48.dp,
                        iconSize = 26.dp,
                        onClick = onPrevious
                    )

                    // Main Big Frosted Glass Play/Pause Circle
                    LiquidGlassPlayPauseButton(
                        isPlaying = isPlaying,
                        size = 72.dp,
                        iconSize = 36.dp,
                        accentGlow = true,
                        onClick = onPlayPause
                    )

                    // Next Track
                    LiquidGlassIconButton(
                        icon = Icons.Rounded.SkipNext,
                        size = 48.dp,
                        iconSize = 26.dp,
                        onClick = onNext
                    )

                    // Repeat
                    LiquidGlassIconButton(
                        icon = if (repeatMode is RepeatState.One) Icons.Rounded.RepeatOne else Icons.Rounded.Repeat,
                        size = 42.dp,
                        iconSize = 20.dp,
                        tint = if (repeatMode !is RepeatState.None) Color(0xFF00E5FF) else Color(0xFF94A3B8),
                        onClick = onToggleRepeat
                    )
                }
            }

            // Next Songs Queue Preview Section
            if (queueTracks.isNotEmpty()) {
                item(key = "next_songs_header") {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Next Songs",
                            style = typo().titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.2.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "${queueTracks.size} tracks",
                            style = typo().bodySmall.copy(
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        )
                    }
                }

                val upcoming: List<Track> = queueTracks.drop(currentQueueIndex + 1).take(5)
                itemsIndexed(
                    items = upcoming,
                    key = { index: Int, t: Track -> "next_${t.videoId}_$index" }
                ) { index: Int, nextTrack: Track ->
                    LiquidGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 3.dp),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = Color(0x18151D30),
                        borderBrush = GlassBorderGradient,
                        onClick = {
                            onTrackSelect(currentQueueIndex + 1 + index)
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33000000))
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(nextTrack.thumbnails?.lastOrNull()?.url ?: "")
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .crossfade(200)
                                        .build(),
                                    contentDescription = nextTrack.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nextTrack.title,
                                    style = typo().bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp
                                    ),
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = nextTrack.artists?.joinToString(", ") { it.name } ?: "",
                                    style = typo().bodySmall.copy(fontSize = 11.5.sp),
                                    color = Color(0xFF94A3B8),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Text(
                                text = "03:12",
                                style = typo().bodySmall.copy(fontSize = 12.sp),
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        }
    }
}
