package com.maxrave.simpmusic.ui.component.glass

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val GlassSurfaceGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x332B324B),
        Color(0x1A141A2E),
    )
)

val GlassBorderGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x4DFFFFFF),
        Color(0x14FFFFFF),
        Color(0x08FFFFFF),
        Color(0x2E8ECAE6),
    )
)

val GlassGlowCyanBorder = Brush.linearGradient(
    colors = listOf(
        Color(0x6600E5FF),
        Color(0x228ECAE6),
        Color(0x14FFFFFF),
        Color(0x4D8ECAE6),
    )
)

val GlassGlowPurpleBorder = Brush.linearGradient(
    colors = listOf(
        Color(0x66B388FF),
        Color(0x227C4DFF),
        Color(0x14FFFFFF),
        Color(0x4DB388FF),
    )
)

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color(0x30151C33),
    borderBrush: Brush = GlassBorderGradient,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .then(
                if (elevation > 0.dp) {
                    Modifier.shadow(elevation, shape, clip = false)
                } else {
                    Modifier
                }
            )
            .clip(shape)
            .background(backgroundColor)
            .border(
                border = BorderStroke(borderWidth, borderBrush),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = Color.White.copy(alpha = 0.3f)),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        content = content
    )
}
