package com.diverging.futures.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diverging.futures.data.Site
import com.diverging.futures.data.sampleSites
import com.diverging.futures.ui.components.LogoIcon

@Composable
fun IntroScreen(onSiteSelected: (Site) -> Unit) {
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
                TextButton(onClick = { /* Skip */ }) {
                    Text("Skip", color = Color(0xFF4A5565), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select a Site",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A0A0A),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Use your phone's camera to activate AR features at these locations.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF364153),
                    lineHeight = 20.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleSites) { site ->
                    SiteSelectionCard(site = site, onClick = { onSiteSelected(site) })
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF9FAFB))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "About Sites",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFC4785A)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Each site contains geo-locked AR experiences. Stand at the designated location to see the futures unfold through your camera.",
                                fontSize = 12.sp,
                                color = Color(0xFF4A5565),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SiteSelectionCard(site: Site, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !site.isComingSoon, onClick = onClick)
            .alpha(if (site.isComingSoon) 0.6f else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, if (site.isComingSoon) Color(0xFFF3F4F6) else Color(0xFFE5E7EB))
    ) {
        Column(modifier = Modifier.padding(26.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = if (site.isComingSoon) Color(0xFF99A1AF) else Color(0xFFC8FF00),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = site.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0A0A0A)
                        )
                        Text(
                            text = site.location,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6A7282)
                        )
                    }
                }
                
                if (!site.isComingSoon) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF99A1AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (site.isComingSoon) {
                HorizontalDivider(color = Color(0xFFE5E7EB))
                Text(
                    text = "Coming Soon",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6A7282),
                    modifier = Modifier.padding(top = 18.dp)
                )
            } else {
                Text(
                    text = "Shellmound Site – A sacred Ohlone burial ground",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF364153),
                    lineHeight = 23.sp
                )
                
                Spacer(modifier = Modifier.height(13.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .fillMaxHeight()
                                .background(Color(0xFFC8FF00))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "4 lenses available",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4A5565)
                    )
                }
            }
        }
    }
}
