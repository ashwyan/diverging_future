package com.diverging.futures.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diverging.futures.data.LensType
import com.diverging.futures.ui.components.LogoIcon

@Composable
fun HomeScreen(onGetStarted: () -> Unit) {
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
                // Vector icon placeholder (Menu)
                IconButton(onClick = {}) {
                    // Simulating the menu icon from CSS
                    Column(modifier = Modifier.size(24.dp), verticalArrangement = Arrangement.Center) {
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFF0A0A0A)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFF0A0A0A)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFF0A0A0A)))
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(25.dp, 24.dp, 25.dp, 24.dp)
            ) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8FF00)),
                    shape = RoundedCornerShape(33554432.dp) // Large radius from CSS
                ) {
                    Text(
                        text = "Get Started",
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
                .padding(horizontal = 24.dp), // Responsive padding instead of 70.dp
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Diverging",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color(0xFF0A0A0A)
            )
            
            Text(
                text = "An AR Civic Engagement App",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF364153),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            LogoIcon(modifier = Modifier.sizeIn(maxWidth = 320.dp, maxHeight = 320.dp).fillMaxWidth(0.9f))

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Sites of Imagination",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This place-based experience exposes hidden histories, imagines alternative futures, and asks you to take action.",
                    fontSize = 14.sp,
                    color = Color(0xFF1E2939),
                    lineHeight = 23.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose to visit sites or explore online. Swipe through four augmented reality (AR) futures for the site. Then vote on options and sign a petition.",
                    fontSize = 14.sp,
                    color = Color(0xFF1E2939),
                    lineHeight = 23.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Lenses of Possibility",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                LensLegendItem(LensType.WHAT_IS)
                LensLegendItem(LensType.WHAT_SHOULD)
                LensLegendItem(LensType.WHAT_COULD)
                LensLegendItem(LensType.WHAT_IF)
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LensLegendItem(type: LensType) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(12.dp)
                .background(type.color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = type.label,
                color = type.color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = type.subtitle,
                color = Color(0xFF364153),
                fontSize = 12.sp
            )
        }
    }
}
