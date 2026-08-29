package com.maxrave.simpmusic.ui.screen.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassBackground
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassIconButton
import com.maxrave.simpmusic.ui.navigation.destination.home.SettingsDestination
import com.maxrave.simpmusic.ui.theme.typo
import com.maxrave.simpmusic.viewModel.HomeViewModel
import com.maxrave.simpmusic.viewModel.SharedViewModel

@Composable
fun HomeScreenStyle2(
    navController: NavController,
    viewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    scrollState: LazyListState,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
) {
    val homeData by viewModel.homeItemList.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val nowPlayingData by sharedViewModel.nowPlayingState.collectAsStateWithLifecycle()
    val controllerState by sharedViewModel.controllerState.collectAsStateWithLifecycle()

    val songItems = homeData.flatMap { it.contents }.filterNotNull().take(25)

    LiquidGlassBackground {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(bottom = 140.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar: Collapse chevron, drag pill, 3 dots menu
            item(key = "style2_topbar") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LiquidGlassIconButton(
                            icon = Icons.Rounded.KeyboardArrowDown,
                            size = 38.dp,
                            iconSize = 22.dp,
                            onClick = {}
                        )

                        // Center drag handle pill
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.40f))
                        )

                        LiquidGlassIconButton(
                            icon = Icons.Rounded.MoreVert,
                            size = 38.dp,
                            iconSize = 20.dp,
                            onClick = {
                                navController.navigate(SettingsDestination)
                            }
                        )
                    }
                }
            }

            // Minimalist Song List with clean dividers
            if (songItems.isNotEmpty()) {
                itemsIndexed(songItems, key = { index, item -> "style2_song_${item.videoId ?: index}" }) { index, song ->
                    val isPlayingThis = nowPlayingData?.track?.videoId == song.videoId
                    val isCurrentPlaying = isPlayingThis && controllerState.isPlaying
                    val duration = "03:${(20 + (index * 7) % 39).toString().padStart(2, '0')}"

                    Style2SongRow(
                        title = song.title ?: "Song Title",
                        artist = song.artists?.joinToString(", ") { it.name } ?: "Artist",
                        thumbnailUrl = song.thumbnails?.lastOrNull()?.url ?: "",
                        duration = duration,
                        isPlaying = isCurrentPlaying,
                        isCurrentTrack = isPlayingThis,
                        onClick = {
                            val track: Track = song.toTrack()
                            val allTracks: List<Track> = songItems.map { it.toTrack() }
                            viewModel.setQueueData(
                                QueueData.Data(
                                    listTracks = allTracks,
                                    firstPlayedTrack = track,
                                    playlistId = "STYLE2_QUEUE",
                                    playlistName = "Now Playing Queue",
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
fun Style2SongRow(
    title: String,
    artist: String,
    thumbnailUrl: String,
    duration: String,
    isPlaying: Boolean,
    isCurrentTrack: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art with rounded square corner
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x331E293B))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(thumbnailUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(200)
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
                            tint = Color(0xFFA855F7),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title & Artist Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typo().bodyMedium.copy(
                        fontWeight = if (isCurrentTrack) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp
                    ),
                    color = if (isCurrentTrack) Color(0xFFC084FC) else Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = artist,
                    style = typo().bodySmall.copy(
                        fontSize = 12.5.sp
                    ),
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Duration on the right
            Text(
                text = duration,
                style = typo().bodySmall.copy(
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color(0xFF64748B)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(start = 82.dp, end = 20.dp),
            thickness = 0.6.dp,
            color = Color.White.copy(alpha = 0.07f)
        )
    }
}
