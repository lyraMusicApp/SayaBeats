package com.maxrave.simpmusic.ui.component.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.maxrave.domain.data.model.home.HomeItem
import com.maxrave.simpmusic.ui.component.glass.GlassBorderGradient
import com.maxrave.simpmusic.ui.component.glass.GlassGlowCyanBorder
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassCard
import com.maxrave.simpmusic.ui.component.glass.LiquidGlassIconButton
import com.maxrave.simpmusic.ui.theme.typo

data class FeaturedCardData(
    val title: String,
    val description: String,
    val imageUrl: String,
    val gradientColors: List<Color>,
    val onClickPlay: () -> Unit = {},
    val onClickCard: () -> Unit = {},
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedCardCarousel(
    cards: List<FeaturedCardData>,
    modifier: Modifier = Modifier,
    onCardClick: ((FeaturedCardData) -> Unit)? = null,
) {
    if (cards.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { cards.size }
    )

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) { page ->
            val card = cards.getOrNull(page) ?: return@HorizontalPager
            FeaturedCardItem(
                card = card,
                onClick = {
                    onCardClick?.invoke(card) ?: card.onClickCard()
                }
            )
        }

        // Pager indicator dots
        if (cards.size > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(cards.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val width = if (isSelected) 20.dp else 6.dp
                    val color = if (isSelected) Color(0xFF38BDF8) else Color(0x33FFFFFF)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(6.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedCardItem(
    card: FeaturedCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isLiked by remember { mutableStateOf(false) }

    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = Color.Transparent,
        borderBrush = GlassGlowCyanBorder,
        onClick = onClick
    ) {
        // Gradient Glass Card Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = card.gradientColors
                    )
                )
        )

        // Subtle glassy specular sheen on top
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.40f)
                        )
                    )
                )
        )

        // Card Content Row: Text + Controls on Left, popping Artwork on Right
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left content column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = card.title,
                        style = typo().titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 0.3.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.description,
                        style = typo().bodySmall.copy(
                            fontSize = 11.5.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color(0xFFD1D5DB),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Action icons row (Play circle, Heart, Download, More)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Glass Play Button
                    FeaturedMiniGlassButton(
                        icon = Icons.Rounded.PlayArrow,
                        onClick = card.onClickPlay,
                        isPrimary = true
                    )

                    // Glass Heart Button
                    FeaturedMiniGlassButton(
                        icon = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        tint = if (isLiked) Color(0xFFFF4D6D) else Color.White,
                        onClick = { isLiked = !isLiked }
                    )

                    // Glass Download Button
                    FeaturedMiniGlassButton(
                        icon = Icons.Rounded.ArrowDownward,
                        onClick = {}
                    )

                    // Glass More Button
                    FeaturedMiniGlassButton(
                        icon = Icons.Rounded.MoreVert,
                        onClick = {}
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right side artwork image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x33000000))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(card.imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(300)
                        .build(),
                    contentDescription = card.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun FeaturedMiniGlassButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    tint: Color = Color.White,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    val bg = if (isPrimary) {
        Brush.linearGradient(
            listOf(
                Color(0x772563EB),
                Color(0x551D4ED8)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                Color(0x33FFFFFF),
                Color(0x15FFFFFF)
            )
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .size(34.dp)
            .clip(CircleShape)
            .background(bg)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White.copy(alpha = 0.35f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(17.dp)
        )
    }
}
