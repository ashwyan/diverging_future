package com.diverging.futures.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.diverging.futures.data.LensType
import com.diverging.futures.ui.components.LogoIcon

@Composable
fun HomeScreen(onGetStarted: () -> Unit) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo placeholder
                LogoIcon(modifier = Modifier.size(32.dp))
                IconButton(onClick = { /* Handle menu */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        },
        bottomBar = {
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9FF00)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Get Started",
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
                text = "Diverging",
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "An AR Civic Engagement App",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            // Central Logo
            LogoIcon(modifier = Modifier.size(120.dp))

            Spacer(modifier = Modifier.height(64.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sites of Imagination",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This place-based experience exposes hidden histories, imagines alternative futures, and asks you to take action.",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose to visit sites or explore online. Swipe through four augmented reality (AR) futures for the site. Then vote on options and sign a petition.",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Lenses of Possibility",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                LensLegendItem(LensType.WHAT_IS, "Present condition")
                LensLegendItem(LensType.WHAT_SHOULD, "Desired, achievable")
                LensLegendItem(LensType.WHAT_COULD, "Imaginative path")
                LensLegendItem(LensType.WHAT_IF, "Radical reimagining")
            }
        }
    }
}

@Composable
fun LensLegendItem(type: LensType, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(type.color, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = type.label,
                color = type.color,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onGetStarted = {})
}
