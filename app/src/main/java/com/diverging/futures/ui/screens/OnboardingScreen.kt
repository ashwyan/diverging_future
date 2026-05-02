package com.diverging.futures.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diverging.futures.ui.components.LogoIcon
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onFinish: () -> Unit, onSkip: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LogoIcon(modifier = Modifier.size(40.dp))
                TextButton(onClick = onSkip) {
                    Text("Skip", color = Color(0xFF4A5565), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                // Page Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(4) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color(0xFFC8FF00) else Color(0xFFE5E5E5)
                        val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(width = width, height = 8.dp)
                        )
                    }
                }

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isFirst = pagerState.currentPage == 0
                    val isLast = pagerState.currentPage == 3

                    // Back
                    IconButton(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                        enabled = !isFirst,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = if (!isFirst) BorderStroke(2.dp, Color(0xFFE5E7EB)) else null
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.alpha(if (isFirst) 0.3f else 1f)) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                            }
                        }
                    }

                    // Next / Start
                    Button(
                        onClick = {
                            if (isLast) onFinish()
                            else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8FF00)),
                        shape = RoundedCornerShape(33554432.dp)
                    ) {
                        Text(
                            if (isLast) "Start Exploring" else "Next",
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    // Forward Icon Button
                    IconButton(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        enabled = !isLast,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(2.dp, if (!isLast) Color(0xFFC8FF00) else Color(0xFFE5E7EB))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward", tint = if (!isLast) Color(0xFFC8FF00) else Color(0xFFE5E7EB))
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            OnboardingPage(page)
        }
    }
}

@Composable
fun OnboardingPage(page: Int) {
    val title = when (page) {
        0 -> "SELECT A SITE"
        1 -> "SWIPE THROUGH FUTURES"
        2 -> "VOTE ON OUTCOMES"
        else -> "ADD YOUR SIGNATURE"
    }
    
    val description = when (page) {
        0 -> "Point your phone's camera at a geo-locked location to activate AR features."
        1 -> "Swipe horizontally to view four speculative lenses of potential futures for the site."
        2 -> "Select your preferred future vision by voting. Your voice helps shape community design."
        else -> "Sign the petition to support your vision. Signatures are shared with stakeholders."
    }

    val themeColor = when (page) {
        0 -> Color(0xFF5B7FA6)
        1 -> Color(0xFF5C8C6E)
        2 -> Color(0xFFB8922A)
        else -> Color(0xFFC4785A)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            OnboardingIcon(page, themeColor)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = themeColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            color = Color(0xFF364153),
            textAlign = TextAlign.Center,
            lineHeight = 23.sp
        )
        
        Spacer(modifier = Modifier.height(100.dp)) // Offset to move content slightly up
    }
}

@Composable
fun OnboardingIcon(page: Int, color: Color) {
    Canvas(modifier = Modifier.size(48.dp)) {
        val w = size.width
        val h = size.height
        val strokeWidth = 4.dp.toPx()

        when (page) {
            0 -> {
                // Select Site Icon (Location-ish)
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.5f, h * 0.1f)
                        lineTo(w * 0.8f, h * 0.4f)
                        lineTo(w * 0.5f, h * 0.9f)
                        lineTo(w * 0.2f, h * 0.4f)
                        close()
                    },
                    color = color,
                    style = Stroke(width = strokeWidth)
                )
                drawCircle(color = color, radius = w * 0.1f, center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.4f), style = Stroke(width = strokeWidth))
            }
            1 -> {
                // Swipe Icon (Horizontal Arrows)
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.1f, h * 0.5f)
                        lineTo(w * 0.9f, h * 0.5f)
                        moveTo(w * 0.3f, h * 0.3f)
                        lineTo(w * 0.1f, h * 0.5f)
                        lineTo(w * 0.3f, h * 0.7f)
                        moveTo(w * 0.7f, h * 0.3f)
                        lineTo(w * 0.9f, h * 0.5f)
                        lineTo(w * 0.7f, h * 0.7f)
                    },
                    color = color,
                    style = Stroke(width = strokeWidth)
                )
            }
            2 -> {
                // Vote Icon (Box with check)
                drawRect(color = color, style = Stroke(width = strokeWidth))
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.3f, h * 0.5f)
                        lineTo(w * 0.45f, h * 0.7f)
                        lineTo(w * 0.75f, h * 0.3f)
                    },
                    color = color,
                    style = Stroke(width = strokeWidth)
                )
            }
            else -> {
                // Signature Icon (Quill-ish)
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.2f, h * 0.8f)
                        lineTo(w * 0.8f, h * 0.2f)
                        moveTo(w * 0.2f, h * 0.8f)
                        lineTo(w * 0.4f, h * 0.8f)
                        moveTo(w * 0.2f, h * 0.8f)
                        lineTo(w * 0.2f, h * 0.6f)
                    },
                    color = color,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
    }
}
