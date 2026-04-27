package com.diverging.futures.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun LogoIcon(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    val gifResId = context.resources.getIdentifier("diverging_logo_gif", "drawable", context.packageName)
    
    if (gifResId != 0) {
        AsyncImage(
            model = gifResId,
            contentDescription = "Animated Logo",
            imageLoader = imageLoader,
            modifier = modifier
        )
    } else {
        // Fallback to drawn logo if GIF is missing
        Canvas(modifier = modifier) {
            val center = Offset(size.width / 2, size.height)
            val colors = listOf(
                Color(0xFFD17F64),
                Color(0xFFC2A13D),
                Color(0xFF5E947A),
                Color(0xFFA8D900)
            )
            val angles = listOf(-60f, -30f, 0f, 30f, 60f)
            
            angles.forEachIndexed { index, angle ->
                val color = colors[index % colors.size]
                val rad = Math.toRadians(angle.toDouble() - 90.0)
                val end = Offset(
                    (center.x + Math.cos(rad) * size.width).toFloat(),
                    (center.y + Math.sin(rad) * size.height * 0.8f).toFloat()
                )
                drawLine(
                    color = color,
                    start = center,
                    end = end,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            
            drawCircle(
                color = Color.Black,
                radius = 3.dp.toPx(),
                center = center
            )
        }
    }
}
