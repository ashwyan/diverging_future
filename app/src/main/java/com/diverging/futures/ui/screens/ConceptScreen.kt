package com.diverging.futures.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.diverging.futures.ui.components.LogoIcon
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun ConceptScreen(onContinue: () -> Unit, onSkip: () -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                LogoIcon(modifier = Modifier.size(32.dp))
                TextButton(onClick = onSkip) {
                    Text("Skip", color = Color.Gray)
                }
            }
        },
        bottomBar = {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9FF00)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Continue",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Concept",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Cone of Possibility",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Dunne & Raby, 2013",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            ConeOfPossibilityDiagram(modifier = Modifier.size(300.dp))

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "This app is rooted in speculative design, or \"what if\" scenarios, to rethink the world through wider lenses. Anthony Dunne and Fiona Raby’s Cone of Possibility is a design tool that maps future scenarios into four categories:",
                fontSize = 15.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ConceptCategoryItem(Color(0xFFA8D900), "Possible", "the widest space of critical imagination")
                ConceptCategoryItem(Color(0xFF5E947A), "Plausible", "near-future visions of what could happen")
                ConceptCategoryItem(Color(0xFFD17F64), "Probable", "what will likely happen without intervention")
                ConceptCategoryItem(Color(0xFFC2A13D), "Preferable", "the middle ground for designers to play in")
            }
        }
    }
}

@Composable
fun ConceptCategoryItem(color: Color, name: String, description: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$name — ",
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ConeOfPossibilityDiagram(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val startX = 20.dp.toPx()
        val centerY = size.height / 2
        val endX = size.width - 60.dp.toPx()
        val coneHeight = 200.dp.toPx()

        // Possible (Widest - Light Greenish)
        val possiblePath = Path().apply {
            moveTo(startX, centerY)
            lineTo(endX, centerY - coneHeight / 2)
            lineTo(endX, centerY + coneHeight / 2)
            close()
        }
        drawPath(possiblePath, Color(0xFFD9FF00).copy(alpha = 0.3f), style = Fill)

        // Plausible (Medium - Greenish)
        val plausiblePath = Path().apply {
            moveTo(startX, centerY)
            lineTo(endX, centerY - coneHeight * 0.35f)
            lineTo(endX, centerY + coneHeight * 0.35f)
            close()
        }
        drawPath(plausiblePath, Color(0xFF5E947A).copy(alpha = 0.4f), style = Fill)

        // Probable (Thinner - Brownish)
        val probablePath = Path().apply {
            moveTo(startX, centerY)
            lineTo(endX, centerY)
            lineTo(endX, centerY + coneHeight * 0.25f)
            close()
        }
        drawPath(probablePath, Color(0xFFD17F64).copy(alpha = 0.4f), style = Fill)

        // Preferable (Small slice - Yellowish)
        val preferablePath = Path().apply {
            moveTo(startX, centerY)
            lineTo(endX, centerY + coneHeight * 0.1f)
            lineTo(endX, centerY + coneHeight * 0.25f)
            close()
        }
        drawPath(preferablePath, Color(0xFFC2A13D).copy(alpha = 0.4f), style = Fill)

        // Labels
        // (In a real implementation, we'd use drawText or similar, but for now placeholders)
        
        // Present dot
        drawCircle(Color.Black, 6.dp.toPx(), Offset(startX, centerY))
    }
}

@Preview(showBackground = true)
@Composable
fun ConceptScreenPreview() {
    ConceptScreen(onContinue = {}, onSkip = {}, onBack = {})
}
