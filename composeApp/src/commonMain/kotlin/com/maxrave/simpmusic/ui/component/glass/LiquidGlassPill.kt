package com.maxrave.simpmusic.ui.component.glass

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxrave.simpmusic.ui.theme.typo

val ActivePillGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF2563EB),
        Color(0xFF7C3AED),
    )
)

val InactivePillBorder = Brush.linearGradient(
    colors = listOf(
        Color(0x35FFFFFF),
        Color(0x10FFFFFF),
    )
)

@Composable
fun LiquidGlassPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFFC7D2FE),
        animationSpec = tween(200),
        label = "pillTextColor"
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .then(
                if (isSelected) {
                    Modifier
                        .background(ActivePillGradient)
                        .border(
                            border = BorderStroke(
                                1.dp,
                                Brush.linearGradient(
                                    listOf(
                                        Color(0x8093C5FD),
                                        Color(0x30C084FC)
                                    )
                                )
                            ),
                            shape = CircleShape
                        )
                } else {
                    Modifier
                        .background(Color(0x221E293B))
                        .border(
                            border = BorderStroke(1.dp, InactivePillBorder),
                            shape = CircleShape
                        )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White.copy(alpha = 0.3f)),
                onClick = onClick
            )
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typo().bodyMedium.copy(
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                letterSpacing = 0.2.sp
            ),
            color = textColor
        )
    }
}
