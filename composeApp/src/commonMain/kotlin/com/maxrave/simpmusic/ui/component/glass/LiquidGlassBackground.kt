package com.maxrave.simpmusic.ui.component.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val DeepSpaceDarkBg = Color(0xFF070913)
val DarkPurpleGlow = Color(0xFF3B1D61)
val DeepTealGlow = Color(0xFF0F3E4D)
val ElectricBlueGlow = Color(0xFF1E3A8A)

@Composable
fun LiquidGlassBackground(
    modifier: Modifier = Modifier,
    accentColor: Color? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DeepSpaceDarkBg,
                        Color(0xFF0B0E1E),
                        DeepSpaceDarkBg
                    )
                )
            )
    ) {
        // Ambient soft light blobs in background for glass refraction
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-40).dp, y = (-20).dp)
                .size(260.dp)
                .blur(90.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accentColor?.copy(alpha = 0.35f) ?: DeepTealGlow.copy(alpha = 0.45f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .size(320.dp)
                .blur(110.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            DarkPurpleGlow.copy(alpha = 0.40f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 80.dp)
                .size(350.dp)
                .blur(100.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            ElectricBlueGlow.copy(alpha = 0.30f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        content()
    }
}
