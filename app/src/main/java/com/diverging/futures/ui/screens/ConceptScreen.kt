package com.diverging.futures.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.diverging.futures.ui.components.LogoIcon
import com.diverging.futures.ui.components.VideoPlayer
import com.diverging.futures.R

@Composable
fun ConceptScreen(onContinue: () -> Unit, onSkip: () -> Unit) {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 25.dp, start = 24.dp, end = 24.dp)
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8FF00)),
                    shape = RoundedCornerShape(33554432.dp)
                ) {
                    Text(
                        text = "Continue",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Concept",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color(0xFF0A0A0A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // MP4 Logo Video
            // Note: Place your diverging_logo.mp4 file in app/src/main/res/raw/
            VideoPlayer(
                videoResId = R.raw.diverging_logo,
                modifier = Modifier.size(200.dp).clip(RoundedCornerShape(16.dp))
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Cone of Possibility",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF0A0A0A),
                modifier = Modifier.padding(top = 12.dp)
            )
            
            Text(
                text = "Dunne & Raby, 2013",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF4A5565),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
//
//            Box(modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp, max = 300.dp).padding(horizontal = 16.dp)) {
//                ConeOfPossibilityDiagram(modifier = Modifier.fillMaxSize())
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "This app is rooted in speculative design, or \"what if\" scenarios, to rethink the world through wider lenses. Anthony Dunne and Fiona Raby’s Cone of Possibility is a design tool that maps future scenarios into four categories:",
                    fontSize = 14.sp,
                    color = Color(0xFF364153),
                    lineHeight = 23.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                ConceptCategoryDetail(Color(0xFF6B8E00), "Possible", "the widest space of critical imagination")
                ConceptCategoryDetail(Color(0xFF5C8C6E), "Plausible", "near-future visions of what could happen")
                ConceptCategoryDetail(Color(0xFFC4785A), "Probable", "what will likely happen without intervention")
                ConceptCategoryDetail(Color(0xFFB8922A), "Preferable", "the middle ground for designers to play in")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ConceptCategoryDetail(color: Color, name: String, description: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$name — ",
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = description,
            color = color,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ConeOfPossibilityDiagram(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val startX = size.width * 0.08f
        val centerY = size.height * 0.5f
        
        // Colors and Gradients from CSS
        val colorProbable = Color(0xFFC4785A)
        val colorPreferable = Color(0xFFB8922A)
        val colorPlausible = Color(0xFF5C8C6E)
        val colorPossible = Color(0xFFC8FF00)

        // Possible (Top-most)
        drawPath(
            path = Path().apply {
                moveTo(startX, centerY)
                lineTo(size.width * 0.75f, size.height * 0.1f)
                lineTo(size.width * 0.75f, size.height * 0.5f)
                close()
            },
            brush = Brush.horizontalGradient(listOf(colorPossible.copy(0.6f), colorPossible.copy(0.3f)))
        )
        
        // Plausible
        drawPath(
            path = Path().apply {
                moveTo(startX, centerY)
                lineTo(size.width * 0.7f, size.height * 0.26f)
                lineTo(size.width * 0.7f, size.height * 0.5f)
                close()
            },
            brush = Brush.horizontalGradient(listOf(colorPlausible.copy(0.6f), colorPlausible.copy(0.3f)))
        )
        
        // Probable
        drawPath(
            path = Path().apply {
                moveTo(startX, centerY)
                lineTo(size.width * 0.7f, size.height * 0.74f)
                lineTo(size.width * 0.7f, size.height * 0.5f)
                close()
            },
            brush = Brush.horizontalGradient(listOf(colorProbable.copy(0.6f), colorProbable.copy(0.3f)))
        )
        
        // Preferable
        drawPath(
            path = Path().apply {
                moveTo(startX, centerY)
                lineTo(size.width * 0.75f, size.height * 0.9f)
                lineTo(size.width * 0.75f, size.height * 0.5f)
                close()
            },
            brush = Brush.horizontalGradient(listOf(colorPreferable.copy(0.6f), colorPreferable.copy(0.3f)))
        )

        // Present line
        drawLine(
            color = Color(0xFF2A2A2A),
            start = Offset(startX, centerY - 10),
            end = Offset(startX, centerY + 10),
            strokeWidth = 2.dp.toPx()
        )
    }
}
